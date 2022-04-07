package tk.pokatomnik.scpfoundation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tk.pokatomnik.scpfoundation.components.LazyList
import tk.pokatomnik.scpfoundation.domain.PageInfo

@Composable
fun LazyPagesList(
    loading: Boolean = false,
    forceRefresh: () -> Unit = {},
    items: List<PageInfo>,
    onSelectURL: (url: String) -> Unit = {}
) {
    val scrollRefreshState = rememberSwipeRefreshState(loading)
    val selectedURLs = remember{ mutableStateListOf<String>() }

    SwipeRefresh(
        state = scrollRefreshState,
        onRefresh = forceRefresh,
        modifier = Modifier.fillMaxSize(),
        swipeEnabled = !loading
    ) {
        LazyList(
            list = items,
            onClick = { onSelectURL(it.url) },
            disabled = loading,
        ) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Row { Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    Row {
                        Text(
                            it.author ?: "(Автор неизвестен)",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column(modifier = Modifier.requiredWidth(48.dp).width(48.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            modifier = Modifier.requiredWidth(48.dp).width(48.dp),
                            onClick = {
                                if (selectedURLs.contains(it.url)) {
                                    selectedURLs.remove(it.url)
                                } else {
                                    selectedURLs.add(it.url)
                                }
                            }
                        ) {
                            val icon = if (selectedURLs.contains(it.url)) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = "В Избранное"
                            )
                        }
                    }
                }
            }
        }
    }
}