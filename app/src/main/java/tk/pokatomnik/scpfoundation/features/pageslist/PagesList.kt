package tk.pokatomnik.scpfoundation.features.pageslist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.features.pagesproviders.LocalPagesList

@Composable
fun PagesList(
    title: String,
    hideNavigation: Boolean = false,
    onSelectPageInfo: (pageInfo: PageInfo) -> Unit,
    bottomText: (page: PageInfo) -> String?,
    emptyText: String = ""
) {
    val state = LocalPagesList.current
    val scrollRefreshState = rememberSwipeRefreshState(state.loading)
    val lazyListState = rememberLazyListState()

    LaunchedEffect(state.pageNumber) {
        if ((state.pagedResponse?.documents?.size ?: 0) > 0) {
            lazyListState.animateScrollToItem(0)
        }
    }

    SwipeRefresh(
        state = scrollRefreshState,
        onRefresh = state.forceRefresh,
        modifier = Modifier.fillMaxSize(),
        swipeEnabled = !state.loading,
        indicatorPadding = PaddingValues(top = 80.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .requiredHeight(64.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PageTitle(title = title)
            }
            if (state.pagedResponse?.documents?.size == 0) {
                // Display Empty message
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(emptyText)
                }
            } else {
                Row(modifier = Modifier.weight(1f)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyPagesList(
                            loading = state.loading,
                            pagedResponse = state.pagedResponse ?: PagedResponse(),
                            onSelectPageInfo = onSelectPageInfo,
                            bottomText = bottomText,
                            lazyListState = lazyListState,
                        )
                    }
                }
                if (!hideNavigation) {
                    NavigationButtons(
                        currentPage = state.pageNumber,
                        loading = state.loading,
                        maxPage = state.pagedResponse?.maxPage ?: 1,
                        onExplicitNavigate = state.onExplicitNavigate
                    )
                }
            }
        }
    }
}