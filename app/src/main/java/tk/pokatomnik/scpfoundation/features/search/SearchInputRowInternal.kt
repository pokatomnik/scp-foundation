package tk.pokatomnik.scpfoundation.features.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun SearchInputRowInternal(
    searchBarHeight: Dp,
    enabled: Boolean,
    focusRequester: FocusRequester,
    searchValue: String,
    setSearchValue: (searchValue: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(searchBarHeight)
            .requiredHeight(searchBarHeight)
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
    ) {
        TextField(
            enabled = enabled,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxSize(),
            value = searchValue,
            onValueChange = setSearchValue
        )
    }
}