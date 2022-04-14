package tk.pokatomnik.scpfoundation.features.pagescontext

import androidx.compose.runtime.compositionLocalOf
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.PagedResponseImpl

internal class ContextValue(
    val hasError: Boolean,
    val loading: Boolean,
    val pagedResponse: PagedResponse?,
    val pageNumber: Int,
    val previous: () -> Unit,
    val next: () -> Unit,
    val forceRefresh: () -> Unit
)

internal val LocalPagesList = compositionLocalOf {
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