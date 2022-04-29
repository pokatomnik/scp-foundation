package tk.pokatomnik.scpfoundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

private fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)

@Composable
fun <T : Any> LazyList(
    list: List<T>,
    onClick: (value: T) -> Unit,
    disabled: Boolean = false,
    lazyListState: LazyListState = rememberLazyListState(),
    render: @Composable (value: T) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.scrollEnabled(!disabled),
        state = lazyListState
    ) {
        items(list) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable(
                        enabled = !disabled,
                        onClick = { onClick(it) },
                    )
                    .padding(
                        vertical = 6.dp,
                        horizontal = 16.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                render(it)
            }

        }
    }
}