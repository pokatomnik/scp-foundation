package tk.pokatomnik.scpfoundation.features.pagesproviders

import androidx.compose.runtime.compositionLocalOf
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.PagedResponseImpl

internal class PagesPagination(
    val hasError: Boolean,
    val loading: Boolean,
    val pagedResponse: PagedResponse?,
    val pageNumber: Int,
    val onExplicitNavigate: (pageNumber: Int) -> Unit,
    val forceRefresh: () -> Unit
)

internal val LocalPagesList = compositionLocalOf {
    PagesPagination(
        hasError = false,
        loading = false,
        pagedResponse = PagedResponseImpl(),
        pageNumber = 0,
        onExplicitNavigate = {},
        forceRefresh = {}
    )
}