package tk.pokatomnik.scpfoundation.features.configuration

import androidx.compose.runtime.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.di.http.rememberHttpClient
import tk.pokatomnik.scpfoundation.domain.Configuration

enum class ConfigurationState {
    INITIAL,
    LOADING,
    ERROR,
    HAS_DATA
}

@Composable
fun ConfigurationProvider(
    errorSideEffect: (error: Throwable) -> Unit = {},
    renderOnInitialState: @Composable () -> Unit = {},
    renderOnLoading: @Composable () -> Unit = {},
    renderOnError: @Composable () -> Unit = {},
    renderOnConfigurationExists: @Composable () -> Unit = {}
) {
    val httpClient = rememberHttpClient()
    val (state, setState) = remember { mutableStateOf(ConfigurationState.INITIAL) }
    val (configuration, setConfiguration) = remember { mutableStateOf<Configuration?>(null) }

    fun handleError(error: Throwable) {
        errorSideEffect(error)
        setState(ConfigurationState.ERROR)
    }

    DisposableEffect(Unit) {
        setState(ConfigurationState.LOADING)
        val request = httpClient.getConfiguration()
        request.enqueue(object : Callback<Configuration> {
            override fun onResponse(call: Call<Configuration>, response: Response<Configuration>) {
                val newConfiguration = response.body()
                if (newConfiguration == null) {
                    handleError(Throwable("Server responded with no configuration"))
                } else {
                    setState(ConfigurationState.HAS_DATA)
                    setConfiguration(newConfiguration)
                }
            }
            override fun onFailure(call: Call<Configuration>, t: Throwable) {
                handleError(t)
            }
        })

        onDispose {
            request.cancel()
        }
    }

    when (state) {
        ConfigurationState.INITIAL -> renderOnInitialState()
        ConfigurationState.LOADING -> renderOnLoading()
        ConfigurationState.ERROR -> renderOnError()
        ConfigurationState.HAS_DATA -> {
            configuration?.let {
                CompositionLocalProvider(LocalConfiguration provides configuration) {
                    renderOnConfigurationExists()
                }
            }
        }
    }
}