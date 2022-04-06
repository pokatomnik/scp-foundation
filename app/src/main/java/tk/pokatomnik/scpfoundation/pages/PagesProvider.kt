package tk.pokatomnik.scpfoundation.pages

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.utils.parse
import tk.pokatomnik.scpfoundation.utils.stringify
import kotlin.math.max

class ContextValue(
    val hasError: Boolean,
    val loading: Boolean,
    val items: List<PageInfo>,
    val pageNumber: Int,
    val previous: () -> Unit,
    val next: () -> Unit
)

val LocalPagesList = compositionLocalOf {
    ContextValue(
        hasError = false,
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

    val (hasError, setHasError) = rememberSaveable { mutableStateOf(false) }
    val (loading, setLoading) = rememberSaveable { mutableStateOf(false) }
    var (pages, setPages) = rememberSaveable(
        saver = Saver<MutableState<List<PageInfo>>, List<String>>(
            save = { (pages) ->
                pages.map { stringify(it) }
            },
            restore = { pageJsons ->
                val pages = pageJsons.map {
                    parse<PageInfoImpl>(it)
                }
                mutableStateOf(pages)
            }
        )
    ) { mutableStateOf(listOf()) }

    val (pageNumber, setPageNumber) = remember { mutableStateOf(1) }

    val previous = { setPageNumber(max(1, pageNumber - 1)) }
    val next = { setPageNumber(pageNumber + 1) }

    DisposableEffect(pageNumber) {
        if (loading) {
            return@DisposableEffect onDispose { }
        }

        setHasError(false)
        setLoading(true)

        val request = pagesViewModel.httpClient.pagesService.listPages(pageNumber = pageNumber)
        request.enqueue(object : Callback<List<PageInfo>> {
            override fun onResponse(
                call: Call<List<PageInfo>>,
                response: Response<List<PageInfo>>
            ) {
                setPages(response.body() ?: listOf())
                setLoading(false)
            }

            override fun onFailure(call: Call<List<PageInfo>>, t: Throwable) {
                setLoading(false)
                setHasError(true)
            }
        })
        onDispose {
            request.cancel()
        }
    }
    CompositionLocalProvider(
        LocalPagesList provides ContextValue(
            hasError = hasError,
            loading = loading,
            items = pages,
            pageNumber = pageNumber,
            next = next,
            previous = previous
        )
    ) {
        children()
    }
}