package tk.pokatomnik.scpfoundation.features.pagesproviders

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import tk.pokatomnik.scpfoundation.di.db.rememberDatabase
import tk.pokatomnik.scpfoundation.domain.PageInfoImpl
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.PagedResponseImpl

@Composable
fun HistoryPagesProvider(
    children: @Composable () -> Unit,
) {
    val database = rememberDatabase()
    val scope = rememberCoroutineScope()

    val (hasError, setHasError) = remember { mutableStateOf(false) }
    val (loading, setLoading) = remember { mutableStateOf(false) }
    val (pages, setPages) = remember { mutableStateOf<PagedResponse?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            setHasError(false)
            setLoading(true)
            setPages(null)

            try {
                val favoritesList = database.recentDAO().getAll()
                val pagesList = favoritesList.map { PageInfoImpl(it) }
                setPages(
                    PagedResponseImpl(
                        pages = pagesList,
                        minPage = 1,
                        maxPage = 1
                    )
                )
            } catch (e: Exception) {
                setHasError(true)
            } finally {
                setLoading(false)
            }
        }
    }

    CompositionLocalProvider(
        LocalPagesList provides PagesPagination(
            hasError = hasError,
            loading = loading,
            pagedResponse = pages,
            pageNumber = 1,
            onExplicitNavigate = { },
            forceRefresh = { }
        )
    ) {
        children()
    }
}