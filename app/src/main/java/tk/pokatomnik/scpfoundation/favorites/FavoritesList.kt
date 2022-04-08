package tk.pokatomnik.scpfoundation.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import tk.pokatomnik.scpfoundation.components.LazyList
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.rememberDatabase
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PageInfoImpl
import tk.pokatomnik.scpfoundation.pages.Title

@Composable
fun FavoritesList(onSelectURL: (url: String) -> Unit) {
    val database = rememberDatabase()
    val scope = rememberCoroutineScope()

    val scrollRefreshState = rememberSwipeRefreshState(false)

    val (pages, setPages) = remember { mutableStateOf<List<PageInfo>>(listOf()) }
    val (favoriteURLs, setFavoriteURLs) = remember { mutableStateOf<List<String>>(listOf()) }

    fun refresh() {
        scope.launch {
            val favorites: List<PageInfo> = database.favoritesDAO().getAll().map { PageInfoImpl(it) }
            setPages(favorites)
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    LaunchedEffect(pages) {
        scope.launch {
            val ids = pages.map { it.url }.toTypedArray()
            val favorites = database.favoritesDAO().getByURLs(ids)
            val newFavoriteURLs = favorites.map { it.url }.toList();
            setFavoriteURLs(newFavoriteURLs)
        }
    }

    fun addFavorite(pageInfo: PageInfo) {
        setFavoriteURLs(ArrayList<String>(favoriteURLs).apply { add(pageInfo.url) })
        scope.launch {
            database.favoritesDAO().add(Favorite(pageInfo))
        }
    }

    fun removeFavorite(pageInfo: PageInfo) {
        setFavoriteURLs(ArrayList<String>(favoriteURLs).apply { remove(pageInfo.url) })
        scope.launch {
            database.favoritesDAO().deleteByURL(pageInfo.url)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .requiredHeight(64.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Title(title = "Избранное")
        }
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                SwipeRefresh(
                    state = scrollRefreshState,
                    onRefresh = { refresh() },
                    modifier = Modifier.fillMaxSize(),
                    swipeEnabled = true
                ) {
                    LazyList(
                        list = pages,
                        onClick = { onSelectURL(it.url) },
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
                            Column(modifier = Modifier
                                .requiredWidth(48.dp)
                                .width(48.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .requiredWidth(48.dp)
                                            .width(48.dp),
                                        onClick = {
                                            if (favoriteURLs.contains(it.url)) {
                                                removeFavorite(it)
                                            } else {
                                                addFavorite(it)
                                            }
                                        }
                                    ) {
                                        val icon = if (favoriteURLs.contains(it.url)) {
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
        }
    }
}