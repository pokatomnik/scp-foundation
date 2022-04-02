package tk.pokatomnik.scpfoundation.pages

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class ContextValue(
    error: Throwable?,
    loading: Boolean,
    items: List<PageInfo>,
    val pageNumber: Int,
    val previous: () -> Unit,
    val next: () -> Unit
) : State(
    error,
    loading,
    items
)

val LocalPagesList = compositionLocalOf {
    ContextValue(
        error = null,
        loading = false,
        items = listOf(),
        pageNumber = 0,
        previous = {},
        next = {}
    )
}

@Composable
fun PagesProvider(
    children: @Composable () -> Unit
) {
    val pagesViewModel = hiltViewModel<PagesViewModel>()
    val (state, setState) = remember { mutableStateOf(State.initial()) }
    val (pageNumber, setPageNumber) = remember { mutableStateOf(1) }
    val previous = { setPageNumber(max(1, pageNumber - 1)) }
    val next = { setPageNumber(pageNumber + 1) }

    DisposableEffect(pageNumber) {
        if (state.loading) {
            return@DisposableEffect onDispose { }
        }
        setState(State.loading())
        val request = pagesViewModel.httpClient.pagesService.listPages(pageNumber = pageNumber)
        request.enqueue(object : Callback<List<PageInfo>> {
            override fun onResponse(
                call: Call<List<PageInfo>>,
                response: Response<List<PageInfo>>
            ) {
                setState(State.data(items = response.body() ?: listOf()))
            }

            override fun onFailure(call: Call<List<PageInfo>>, t: Throwable) {
                setState(State.error(t, state.items))
            }
        })
        onDispose {
            request.cancel()
        }
    }
    CompositionLocalProvider(
        LocalPagesList provides ContextValue(
            error = state.error,
            loading = state.loading,
            items = state.items,
            pageNumber = pageNumber,
            next = next,
            previous = previous
        )
    ) {
        children()
    }
}