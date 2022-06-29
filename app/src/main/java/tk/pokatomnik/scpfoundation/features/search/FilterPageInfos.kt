package tk.pokatomnik.scpfoundation.features.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import tk.pokatomnik.scpfoundation.domain.PageInfo

internal suspend fun filterPageInfos(
    searchValue: String,
    pageInfos: List<PageInfo>,
    scope: CoroutineScope
): List<PageInfo> {
    return withContext(scope.coroutineContext) {
        val searchValueClean = searchValue.trim()
        if (searchValueClean.isBlank()) {
            pageInfos
        } else pageInfos.filter { pageInfo ->
            val stringValues = listOf(
                pageInfo.name.lowercase(),
                pageInfo.title.lowercase(),
            )
            stringValues.any { strValue ->
                strValue.contains(searchValueClean)
            }
        }
    }
}