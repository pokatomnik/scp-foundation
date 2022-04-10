package tk.pokatomnik.scpfoundation.features.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tk.pokatomnik.scpfoundation.features.pagescontext.LocalPagesList

@Composable
fun PagesList(
    title: String,
    onSelectURL: (url: String) -> Unit
) {
    val state = LocalPagesList.current
    val scrollRefreshState = rememberSwipeRefreshState(state.loading)

    SwipeRefresh(
        state = scrollRefreshState,
        onRefresh = state.forceRefresh,
        modifier = Modifier.fillMaxSize(),
        swipeEnabled = !state.loading,
        indicatorPadding = PaddingValues(top = 80.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
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
            Row(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                    LazyPagesList(
                        loading = state.loading,
                        pagedResponse = state.pagedResponse,
                        onSelectURL = onSelectURL,
                    )
                }
            }
            NavigationButtons(
                currentPage = state.pageNumber,
                onNextClick = state.next,
                onPreviousClick = state.previous,
                loading = state.loading,
                maxPage = state.pagedResponse.maxPage
            )
        }
    }
}