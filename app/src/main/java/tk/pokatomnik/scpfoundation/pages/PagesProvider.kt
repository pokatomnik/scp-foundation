package tk.pokatomnik.scpfoundation.pages

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val LocalPagesList = compositionLocalOf { listOf<PageInfo>() }

@Composable
fun PagesProvider(children: @Composable () -> Unit) {
    val pagesViewModel = hiltViewModel<PagesViewModel>()
    val (pages, setPages) = remember { mutableStateOf<List<PageInfo>>(listOf()) }

    DisposableEffect(Unit) {
        val request = pagesViewModel.httpClient.pagesService.listPages(0)
        request.enqueue(object : Callback<List<PageInfo>> {
            override fun onResponse(
                call: Call<List<PageInfo>>,
                response: Response<List<PageInfo>>
            ) {
                response.body()?.let { setPages(it) }
            }

            override fun onFailure(call: Call<List<PageInfo>>, t: Throwable) {
                // TODO try again
                val err = t;
            }
        })
        onDispose {
            request.cancel()
        }
    }
    CompositionLocalProvider(LocalPagesList provides pages) {
        children()
    }
}