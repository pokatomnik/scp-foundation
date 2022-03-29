package tk.pokatomnik.scpfoundation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T : Any> LazyList(
    list: List<T>,
    onClick: (value: T) -> Unit,
    render: @Composable (value: T) -> Unit,
) {
    LazyColumn {
        items(list) {
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).clickable(
                    enabled = true,
                    onClick = { onClick(it) },
                ).padding(
                    vertical =  6.dp,
                    horizontal = 16.dp,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                render(it)
            }

        }
    }
}