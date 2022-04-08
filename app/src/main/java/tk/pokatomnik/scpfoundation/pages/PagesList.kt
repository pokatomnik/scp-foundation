package tk.pokatomnik.scpfoundation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PagesList(onSelectURL: (url: String) -> Unit) {
    val state = LocalPagesList.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .requiredHeight(64.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PageTitle(title = "Список документов")
        }
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                LazyPagesList(
                    loading = state.loading,
                    forceRefresh = state.forceRefresh,
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