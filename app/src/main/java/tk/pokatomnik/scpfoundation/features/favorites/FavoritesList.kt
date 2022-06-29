package tk.pokatomnik.scpfoundation.features.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import tk.pokatomnik.scpfoundation.features.pageslist.PageTitle
import tk.pokatomnik.scpfoundation.features.search.SearchFeature

@Composable
fun FavoritesList(
    onSelectPageInfo: (pageInfo: PageInfo) -> Unit
) {
    val database = rememberDatabase()
    val scope = rememberCoroutineScope()

    val scrollRefreshState = rememberSwipeRefreshState(false)

    val (pages, setPages) = remember { mutableStateOf<List<PageInfo>?>(null) }
    val (favoriteURLs, setFavoriteURLs) = remember { mutableStateOf<List<String>>(listOf()) }

    fun refresh() {
        scope.launch {
            val favorites: List<PageInfo> =
                database.favoritesDAO().getAll().map { PageInfo(it) }
            setPages(favorites)
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    LaunchedEffect(pages) {
        scope.launch {
            val ids = (pages ?: listOf()).map { it.name }.toTypedArray()
            val favorites = database.favoritesDAO().getByNames(ids)
            val newFavoriteURLs = favorites.map { it.name }.toList()
            setFavoriteURLs(newFavoriteURLs)
        }
    }

    fun addFavorite(pageInfo: PageInfo) {
        setFavoriteURLs(ArrayList<String>(favoriteURLs).apply { add(pageInfo.name) })
        scope.launch {
            database.favoritesDAO().add(Favorite(pageInfo))
        }
    }

    fun removeFavorite(pageInfo: PageInfo) {
        setFavoriteURLs(ArrayList<String>(favoriteURLs).apply { remove(pageInfo.name) })
        scope.launch {
            database.favoritesDAO().deleteByName(pageInfo.name)
        }
    }

    SearchFeature(pages ?: listOf(), scope) { searchParams ->
        SwipeRefresh(
            state = scrollRefreshState,
            onRefresh = { refresh() },
            modifier = Modifier.fillMaxSize(),
            swipeEnabled = true
        ) {
            Column(
                modifier = Modifier
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
                        PageTitle(title = "ИЗБРАННОЕ")
                    }
                    Column {
                        searchParams.SearchButton()
                    }
                }
                searchParams.SearchInputRow()
                Row(modifier = Modifier.weight(1f)) {
                    if (pages?.size == 0) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("В избранном пусто:(")
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LazyList(
                                list = searchParams.filteredPageInfos,
                                onClick = { onSelectPageInfo(it) },
                            ) {
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row {
                                            Text(
                                                it.title,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Row {
                                            Text(
                                                it.author ?: "(Автор неизвестен)",
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .requiredWidth(48.dp)
                                            .width(48.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            IconButton(
                                                modifier = Modifier
                                                    .requiredWidth(48.dp)
                                                    .width(48.dp),
                                                onClick = {
                                                    if (favoriteURLs.contains(it.name)) {
                                                        removeFavorite(it)
                                                    } else {
                                                        addFavorite(it)
                                                    }
                                                }
                                            ) {
                                                val icon = if (favoriteURLs.contains(it.name)) {
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
    }
}