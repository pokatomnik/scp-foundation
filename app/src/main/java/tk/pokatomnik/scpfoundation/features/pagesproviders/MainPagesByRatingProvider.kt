package tk.pokatomnik.scpfoundation.features.pagesproviders

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.di.http.rememberHttpClient
import tk.pokatomnik.scpfoundation.di.preferences.rememberPreferences
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.PagedResponseImpl

@Composable
fun MainPagesByRatingProvider(
    children: @Composable () -> Unit
) {
    val context = LocalContext.current
    val httpClient = rememberHttpClient()
    val preferencesContainer = rememberPreferences()

    val (hasError, setHasError) = remember { mutableStateOf(false) }
    val (loading, setLoading) = remember { mutableStateOf(false) }
    val (pages, setPages) = remember { mutableStateOf<PagedResponse?>(null) }

    val (pageNumber, setPageNumber) = remember {
        mutableStateOf(preferencesContainer.pagesPreferences.getSavedPage())
    }

    LaunchedEffect(pageNumber) {
        preferencesContainer.pagesPreferences.savePage(pageNumber)
    }

    fun onExplicitNavigate(pageNumber: Int) {
        pages?.let { pages ->
            when {
                pageNumber in pages.minPage..pages.maxPage -> setPageNumber(pageNumber)
                pageNumber > pages.maxPage ->
                    Toast.makeText(context, "Это последняя страница", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Это первая страница", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun refreshByPageNumber(pageNumber: Int, force: Boolean = false): DisposableEffectResult {
        if (loading) {
            return object : DisposableEffectResult {
                override fun dispose() {}
            }
        }

        setHasError(false)
        setLoading(true)

        val request = if (force) {
            httpClient.pagesService.getDataForce(pageNumber)
        } else {
            httpClient.pagesService.getData(pageNumber)
        }

        request.enqueue(object : Callback<PagedResponse> {
            override fun onResponse(
                call: Call<PagedResponse>,
                response: Response<PagedResponse>
            ) {
                setPages(response.body() ?: PagedResponseImpl())
                setLoading(false)
            }

            override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                setLoading(false)
                setHasError(true)
                setPages(PagedResponseImpl())
                setPageNumber(1)
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
        refreshByPageNumber(pageNumber = pageNumber, force = true)
    }

    DisposableEffect(pageNumber) {
        refreshByPageNumber(pageNumber)
    }

    CompositionLocalProvider(
        LocalPagesList provides ContextValue(
            hasError = hasError,
            loading = loading,
            pagedResponse = pages,
            pageNumber = pageNumber,
            onExplicitNavigate = { onExplicitNavigate(it) },
            forceRefresh = forceRefresh
        )
    ) {
        children()
    }
}