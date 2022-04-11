package tk.pokatomnik.scpfoundation.features.tags

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.components.ChipComponent
import tk.pokatomnik.scpfoundation.components.ChipComponentProps
import tk.pokatomnik.scpfoundation.di.http.rememberHttpClient
import tk.pokatomnik.scpfoundation.features.pages.PageTitle

@Composable
fun Tags() {
    val context = LocalContext.current
    val httpClient = rememberHttpClient()

    val tags = remember { mutableStateListOf<String>() }
    val loadingState = remember { mutableStateOf(false) }
    val scrollRefreshState = rememberSwipeRefreshState(loadingState.value)

    fun loadTags(force: Boolean): DisposableEffectResult {
        if (loadingState.value) {
            return object : DisposableEffectResult {
                override fun dispose() {}
            }
        }
        loadingState.value = true

        val request = if (force) {
            httpClient.tagsService.getDataForce(Unit)
        } else {
            httpClient.tagsService.getData(Unit)
        }

        request.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                tags.apply {
                    clear()
                    addAll(response.body() ?: listOf())
                }
                loadingState.value = false
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(context, "Невозможно загрузить список тегов, попробуйте позднее", Toast.LENGTH_SHORT).show()
                loadingState.value = false
            }

        })
        return object : DisposableEffectResult {
            override fun dispose() {
                return request.cancel()
            }
        }
    }

    DisposableEffect(Unit) { loadTags(false) }

    SwipeRefresh(
        state = scrollRefreshState,
        onRefresh = { loadTags(true) },
        modifier = Modifier.fillMaxSize(),
        swipeEnabled = !loadingState.value,
        indicatorPadding = PaddingValues(top = 80.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .requiredHeight(64.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PageTitle(title = "Поиск тегов")
            }
            FlowRow(modifier = Modifier.padding(vertical = 10.dp)) {
                for (tag in tags) {
                    Column(modifier = Modifier.padding(5.dp)) {
                        ChipComponent(
                            props = ChipComponentProps(
                                text = "#$tag"
                            )
                        )
                    }
                }
            }
        }
    }
}