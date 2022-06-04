package tk.pokatomnik.scpfoundation.features.pagesproviders

import androidx.compose.runtime.compositionLocalOf
import tk.pokatomnik.scpfoundation.domain.PagedResponse

internal class ContextValue(
    val hasError: Boolean,
    val loading: Boolean,
    val pagedResponse: PagedResponse?,
    val pageNumber: Int,
    val onExplicitNavigate: (pageNumber: Int) -> Unit,
    val forceRefresh: () -> Unit
)

internal val LocalPagesList = compositionLocalOf {
    ContextValue(
        hasError = false,
        loading = false,
        pagedResponse = PagedResponse(),
        pageNumber = 0,
        onExplicitNavigate = {},
        forceRefresh = {}
    )
}