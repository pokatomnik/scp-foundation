package tk.pokatomnik.scpfoundation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import tk.pokatomnik.scpfoundation.components.LazyList


@Composable
fun PagesList(
    onSelectURL: (url: String) -> Unit,
) {
    LazyList(
        list = LocalPagesList.current,
        onClick = { onSelectURL(it.url) }
    ) {
        Column {
            Row { Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Row { Text(it.author ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        }
    }
}