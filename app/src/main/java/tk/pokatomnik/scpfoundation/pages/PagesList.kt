package tk.pokatomnik.scpfoundation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tk.pokatomnik.scpfoundation.components.LazyList


@Composable
fun PagesList(
    onSelectURL: (url: String) -> Unit,
) {
    val state = LocalPagesList.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                if (state.loading) {
                    CircularProgressIndicator()
                }
                LazyList(
                    list = state.items,
                    onClick = { onSelectURL(it.url) },
                    disabled = state.loading
                ) {
                    Column {
                        Row { Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                        Row {
                            Text(
                                it.author ?: "(Автор неизвестен)",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .height(64.dp)
                .requiredHeight(64.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = state.previous) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "Предыдущая страница")
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text((state.pageNumber).toString())
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = state.next) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "Следующая страница")
                }
            }
        }
    }
}