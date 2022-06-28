package tk.pokatomnik.scpfoundation.features.pageslist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import kotlinx.coroutines.*
import tk.pokatomnik.scpfoundation.components.LazyList
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.rememberDatabase
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PagedResponse

@Composable
internal fun LazyPagesList(
    loading: Boolean = false,
    searchValue: String,
    pagedResponse: PagedResponse,
    onSelectPageInfo: (pageInfo: PageInfo) -> Unit,
    bottomText: (page: PageInfo) -> String?,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val scope = rememberCoroutineScope()
    val favoritesState = remember { mutableStateListOf<String>() }
    val database = rememberDatabase()

    val (filteredDocuments, setFilteredDocuments) = remember {
        mutableStateOf(pagedResponse.documents)
    }

    LaunchedEffect(pagedResponse.documents) {
        setFilteredDocuments(pagedResponse.documents)
    }

    LaunchedEffect(searchValue, pagedResponse.documents) {
        scope.launch {
            val docs = filterPageInfos(
                searchValue,
                pagedResponse.documents,
                scope
            )
            setFilteredDocuments(docs)
        }
    }

    LaunchedEffect(pagedResponse.documents) {
        scope.launch {
            val ids = pagedResponse.documents.map { it.name }.toTypedArray()
            val favorites = database.favoritesDAO().getByNames(ids)
            favoritesState.addAll(favorites.map { it.name }.toList())
        }
    }

    fun addFavorite(pageInfo: PageInfo) {
        favoritesState.add(pageInfo.name)
        scope.launch {
            database.favoritesDAO().add(Favorite(pageInfo))
        }
    }

    fun removeFavorite(pageInfo: PageInfo) {
        favoritesState.remove(pageInfo.name)
        scope.launch {
            database.favoritesDAO().deleteByName(pageInfo.name)
        }
    }

    LazyList(
        list = filteredDocuments,
        onClick = { onSelectPageInfo(it) },
        disabled = loading,
        lazyListState = lazyListState
    ) {
        Row {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxSize(), verticalArrangement = Arrangement.Center
            ) {
                Row { Text(it.title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
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
                            if (favoritesState.contains(it.name)) {
                                removeFavorite(it)
                            } else {
                                addFavorite(it)
                            }
                        }
                    ) {
                        val icon = if (favoritesState.contains(it.name)) {
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

suspend fun filterPageInfos(
    searchValue: String,
    pageInfos: List<PageInfo>,
    scope: CoroutineScope
): List<PageInfo> {
    return withContext(scope.coroutineContext) {
        val searchValueClean = searchValue.trim()
        if (searchValueClean.isBlank()) {
            pageInfos
        } else pageInfos.filter { pageInfo ->
            val stringValues = listOf(
                pageInfo.name.lowercase(),
                pageInfo.title.lowercase(),
            )
            stringValues.any { strValue ->
                strValue.contains(searchValueClean)
            }
        }
    }
}