package tk.pokatomnik.scpfoundation.pages

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
import kotlin.math.max
import kotlin.math.min

class ContextValue(
    val hasError: Boolean,
    val loading: Boolean,
    val pagedResponse: PagedResponse,
    val pageNumber: Int,
    val previous: () -> Unit,
    val next: () -> Unit,
    val forceRefresh: () -> Unit
)

val LocalPagesList = compositionLocalOf {
    ContextValue(
        hasError = false,
        loading = false,
        pagedResponse = PagedResponseImpl(),
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
    val context = LocalContext.current;
    val httpClient = rememberHttpClient()
    val preferencesContainer = rememberPreferences()

    val (hasError, setHasError) = remember { mutableStateOf(false) }
    val (loading, setLoading) = remember { mutableStateOf(false) }
    val (pages, setPages) = remember { mutableStateOf<PagedResponse>(PagedResponseImpl()) }

    val (pageNumber, setPageNumber) = remember {
        mutableStateOf(preferencesContainer.pagesPreferences.getSavedPage())
    }

    LaunchedEffect(pageNumber) {
        preferencesContainer.pagesPreferences.savePage(pageNumber)
    }

    val previous = {
        val previousPage = max(pages.minPage, pageNumber - 1)
        if (previousPage == pageNumber) {
            Toast.makeText(context, "Это первая страница", Toast.LENGTH_SHORT).show();
        } else {
            setPageNumber(previousPage)
        }
    }
    val next = {
        val nextPage = min(pages.maxPage, pageNumber + 1);
        if (nextPage == pageNumber) {
            Toast.makeText(context, "Это последняя страница", Toast.LENGTH_SHORT).show();
        } else {
            setPageNumber(nextPage)
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
            httpClient.pagesService.listPagesForce(pageNumber)
        } else {
            httpClient.pagesService.listPages(pageNumber)
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
            next = next,
            previous = previous,
            forceRefresh = forceRefresh
        )
    ) {
        children()
    }
}