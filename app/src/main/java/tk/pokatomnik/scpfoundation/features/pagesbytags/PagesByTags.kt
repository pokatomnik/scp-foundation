package tk.pokatomnik.scpfoundation.features.pagesbytags

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.di.http.rememberHttpClient
import tk.pokatomnik.scpfoundation.domain.PageByTagsImpl
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.PagedResponseImpl
import tk.pokatomnik.scpfoundation.features.pagescontext.ContextValue
import tk.pokatomnik.scpfoundation.features.pagescontext.LocalPagesList

@Composable
fun MainPagesByTagsProvider(
    tags: Array<String>,
    children: @Composable () -> Unit
) {
    val context = LocalContext.current
    val httpClient = rememberHttpClient()

    val (hasError, setHasError) = remember { mutableStateOf(false) }
    val (loading, setLoading) = remember { mutableStateOf(false) }
    val (pages, setPages) = remember { mutableStateOf<PagedResponse?>(null) }

    fun refreshByPageNumber(
        tags: Array<String>,
        force: Boolean = false
    ): DisposableEffectResult {
        if (loading) {
            return object : DisposableEffectResult {
                override fun dispose() {}
            }
        }

        setHasError(false)
        setLoading(true)

        val request = if (force) {
            httpClient.pagesByTagsService.getDataForce(tags)
        } else {
            httpClient.pagesByTagsService.getData(tags)
        }

        request.enqueue(object : Callback<List<PageByTagsImpl>> {
            override fun onResponse(
                call: Call<List<PageByTagsImpl>>,
                response: Response<List<PageByTagsImpl>>
            ) {
                val items = (response.body() ?: listOf())
                    .map { it.toPage() }
                    .sortedWith { a, b -> a.name.lowercase().compareTo(b.name.lowercase()) }
                val pagedResponse = PagedResponseImpl(
                    maxPage = 1,
                    minPage = 1,
                    pages = items
                )
                setPages(pagedResponse)
                setLoading(false)
            }

            override fun onFailure(call: Call<List<PageByTagsImpl>>, t: Throwable) {
                setLoading(false)
                setHasError(true)
                setPages(PagedResponseImpl())
                Toast.makeText(
                    context,
                    "Ошибка загрузки документов, попробуйте позднее",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return object : DisposableEffectResult {
            override fun dispose() {
                request.cancel()
            }
        }
    }

    val forceRefresh: () -> Unit = {
        refreshByPageNumber(tags = tags, force = true)
    }

    DisposableEffect(Unit) {
        refreshByPageNumber(tags = tags, force = false)
    }

    CompositionLocalProvider(
        LocalPagesList provides ContextValue(
            hasError = hasError,
            loading = loading,
            pagedResponse = pages,
            pageNumber = 1,
            next = {},
            previous = {},
            forceRefresh = forceRefresh
        )
    ) {
        children()
    }
}