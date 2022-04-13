package tk.pokatomnik.scpfoundation.features.pages

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
import kotlinx.coroutines.launch
import tk.pokatomnik.scpfoundation.components.LazyList
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.rememberDatabase
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PagedResponse

@Composable
internal fun LazyPagesList(
    loading: Boolean = false,
    pagedResponse: PagedResponse,
    onSelectURL: (url: String) -> Unit = {},
    bottomText: (page: PageInfo) -> String?,
) {
    val scope = rememberCoroutineScope()
    val favoritesState = remember { mutableStateListOf<String>() }
    val database = rememberDatabase()

    LaunchedEffect(pagedResponse.pages) {
        scope.launch {
            val ids = pagedResponse.pages.map { it.url }.toTypedArray()
            val favorites = database.favoritesDAO().getByURLs(ids)
            favoritesState.addAll(favorites.map { it.url }.toList())
        }
    }

    fun addFavorite(pageInfo: PageInfo) {
        favoritesState.add(pageInfo.url)
        scope.launch {
            database.favoritesDAO().add(Favorite(pageInfo))
        }
    }

    fun removeFavorite(pageInfo: PageInfo) {
        favoritesState.remove(pageInfo.url)
        scope.launch {
            database.favoritesDAO().deleteByURL(pageInfo.url)
        }
    }


    LazyList(
        list = pagedResponse.pages,
        onClick = { onSelectURL(it.url) },
        disabled = loading,
    ) {
        Row {
            Column(modifier = Modifier.weight(1f).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Row { Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                bottomText(it)?.let {
                    Row {
                        Text(
                            text = it,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
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
                            if (favoritesState.contains(it.url)) {
                                removeFavorite(it)
                            } else {
                                addFavorite(it)
                            }
                        }
                    ) {
                        val icon = if (favoritesState.contains(it.url)) {
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