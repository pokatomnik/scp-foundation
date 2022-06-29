package tk.pokatomnik.scpfoundation.features.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.features.inputfocusmanager.useInputFocusManager

interface SearchParams {
    @Composable
    fun SearchButton()
    @Composable
    fun SearchInputRow()
    val filteredPageInfos: List<PageInfo>
}

@Composable
fun SearchFeature(
    pageInfos: List<PageInfo>,
    scope: CoroutineScope,
    children: @Composable (searchParams: SearchParams) -> Unit,
) {
    val (searchInputDisplayed, setSearchInputDisplayed) = remember { mutableStateOf(false) }
    val searchBarHeight by animateDpAsState(
        targetValue = if (searchInputDisplayed) 56.dp else 0.dp
    )
    val inputFocusManager = useInputFocusManager()
    val (searchValue, setSearchValue) = remember { mutableStateOf("") }


    LaunchedEffect(searchInputDisplayed) {
        setSearchValue("")
        if (searchInputDisplayed) {
            inputFocusManager.requestFocus()
        } else {
            inputFocusManager.freeFocus()
        }
    }

    val (filteredDocuments, setFilteredDocuments) = remember {
        mutableStateOf(pageInfos)
    }

    LaunchedEffect(pageInfos) {
        setFilteredDocuments(pageInfos)
    }

    LaunchedEffect(searchValue, pageInfos) {
        scope.launch {
            val docs = filterPageInfos(
                searchValue,
                pageInfos,
                scope
            )
            setFilteredDocuments(docs)
        }
    }

    val searchParams = object : SearchParams {
        @Composable
        override fun SearchButton() {
            SearchButtonInternal(
                searchEnabled = searchInputDisplayed,
                onClick = { setSearchInputDisplayed(!searchInputDisplayed) }
            )
        }

        @Composable
        override fun SearchInputRow() {
            SearchInputRowInternal(
                searchBarHeight = searchBarHeight,
                enabled = searchInputDisplayed,
                focusRequester = inputFocusManager.focusRequester,
                searchValue = searchValue,
                setSearchValue = setSearchValue
            )
        }

        override val filteredPageInfos: List<PageInfo>
            get() = filteredDocuments
    }

    children(searchParams)
}

