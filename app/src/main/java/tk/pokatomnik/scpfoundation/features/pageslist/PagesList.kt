package tk.pokatomnik.scpfoundation.features.pageslist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.features.inputfocusmanager.useInputFocusManager
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
    val (searchInputDisplayed, setSearchInputDisplayed) = remember { mutableStateOf(false) }
    val searchBarHeight by animateDpAsState(
        targetValue = if (searchInputDisplayed) 56.dp else 0.dp
    )
    val inputFocusManager = useInputFocusManager()
    val (searchValue, setSearchValue) = remember { mutableStateOf("") }

    LaunchedEffect(state.pageNumber) {
        if ((state.pagedResponse?.documents?.size ?: 0) > 0) {
            lazyListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(searchInputDisplayed) {
        setSearchValue("")
        if (searchInputDisplayed) {
            inputFocusManager.requestFocus()
        } else {
            inputFocusManager.freeFocus()
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
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    PageTitle(title = title.uppercase())
                }
                Column {
                    IconButton(
                        onClick = {
                            setSearchInputDisplayed(!searchInputDisplayed)
                        },
                        modifier = Modifier
                            .requiredWidth(64.dp)
                            .width(64.dp),
                    ) {
                        Icon(
                            imageVector = if (searchInputDisplayed) Icons.Filled.SearchOff else Icons.Filled.Search,
                            contentDescription = "Поиск"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(searchBarHeight)
                    .requiredHeight(searchBarHeight)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                TextField(
                    enabled = searchInputDisplayed,
                    modifier = Modifier
                        .focusRequester(inputFocusManager.focusRequester)
                        .fillMaxSize(),
                    value = searchValue,
                    onValueChange = setSearchValue
                )
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
                            searchValue = searchValue,
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