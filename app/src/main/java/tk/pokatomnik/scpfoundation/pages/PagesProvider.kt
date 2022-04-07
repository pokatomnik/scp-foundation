package tk.pokatomnik.scpfoundation.pages

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PageInfoImpl
import tk.pokatomnik.scpfoundation.utils.parse
import tk.pokatomnik.scpfoundation.utils.stringify
import kotlin.math.max

class ContextValue(
    val hasError: Boolean,
    val loading: Boolean,
    val items: List<PageInfo>,
    val pageNumber: Int,
    val previous: () -> Unit,
    val next: () -> Unit,
    val forceRefresh: () -> Unit
)

val LocalPagesList = compositionLocalOf {
    ContextValue(
        hasError = false,
        loading = false,
        items = listOf(),
        pageNumber = 0,
        previous = {},
        next = {},
        forceRefresh = {}
    )
}

@Composable
fun PagesProvider(
    children: @Composable () -> Unit
) {
    val pagesViewModel = hiltViewModel<PagesProviderViewModel>()

    val (hasError, setHasError) = rememberSaveable { mutableStateOf(false) }
    val (loading, setLoading) = rememberSaveable { mutableStateOf(false) }
    val (pages, setPages) = rememberSaveable(
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

    val (pageNumber, setPageNumber) = rememberSaveable {
        mutableStateOf(pagesViewModel.preferencesContainer.pagesPreferences.getSavedPage())
    }

    LaunchedEffect(pageNumber) {
        pagesViewModel.preferencesContainer.pagesPreferences.savePage(pageNumber)
    }

    val previous = { setPageNumber(max(1, pageNumber - 1)) }
    val next = { setPageNumber(pageNumber + 1) }

    fun refreshByPageNumber(pageNumber: Int, force: Boolean = false): DisposableEffectResult {
        if (loading) {
            return object : DisposableEffectResult {
                override fun dispose() { }
            }
        }

        setHasError(false)
        setLoading(true)

        val request = if (force) {
            pagesViewModel.httpClient.pagesService.listPagesForce(pageNumber)
        } else {
            pagesViewModel.httpClient.pagesService.listPages(pageNumber)
        }

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

        return object : DisposableEffectResult {
            override fun dispose() {
                request.cancel()
            }
        }
    }

    val forceRefresh: () -> Unit = {
        refreshByPageNumber(pageNumber = pageNumber, force = true)
    }

    DisposableEffect(pageNumber) {
        refreshByPageNumber(pageNumber)
    }

    CompositionLocalProvider(
        LocalPagesList provides ContextValue(
            hasError = hasError,
            loading = loading,
            items = pages,
            pageNumber = pageNumber,
            next = next,
            previous = previous,
            forceRefresh = forceRefresh
        )
    ) {
        children()
    }
}