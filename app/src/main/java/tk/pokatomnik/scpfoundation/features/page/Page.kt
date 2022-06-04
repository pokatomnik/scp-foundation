package tk.pokatomnik.scpfoundation.features.page

import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import tk.pokatomnik.scpfoundation.di.db.rememberDatabase
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.features.configuration.LocalConfiguration

@Composable
fun Page(
    page: PageInfo?,
    navigateBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val database = rememberDatabase()
    val webViewHolder = remember { WebViewHolder() }

    val (loading, setLoading) = remember { mutableStateOf(false) }
    val (inFavorites, setInFavorites) = remember { mutableStateOf(false) }

    val scrollRefreshState = rememberSwipeRefreshState(loading)

    val (buttonsBarVisible, setButtonsBarVisible) = remember { mutableStateOf(true) }
    val actionsBarHeightAnimated by animateDpAsState(if (buttonsBarVisible) 64.dp else 0.dp)

    val (searchVisible, setSearchVisible) = remember { mutableStateOf(false) }
    val searchBarHeightAnimated by animateDpAsState(if (searchVisible) 56.dp else 0.dp)
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalTextInputService.current

    LaunchedEffect(Unit) {
        page?.let { page ->
            scope.launch {
                val exists = database.favoritesDAO().existsByName(page.name)
                setInFavorites(exists)
            }
        }
    }

    LaunchedEffect(searchText) {
        webViewHolder.searchViewAsync(searchText)
    }

    LaunchedEffect(searchVisible) {
        if (searchVisible) {
            searchFocusRequester.requestFocus()
            keyboardController?.showSoftwareKeyboard()
        } else {
            searchFocusRequester.freeFocus()
            keyboardController?.hideSoftwareKeyboard()
        }
    }

    fun upsertAsRecent() {
        page?.let {
            scope.launch {
                database.recentDAO().upsert(it.toRecent())
            }
        }
    }

    fun addToFavorites() {
        setInFavorites(true)
        page?.let { page ->
            scope.launch {
                database.favoritesDAO().add(page.toFavorite())
            }
        }
    }

    fun removeFromFavorites() {
        setInFavorites(false)
        page?.let { page ->
            scope.launch {
                database.favoritesDAO().deleteByName(page.name)
            }
        }
    }

    if (page == null) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(searchBarHeightAnimated)
        ) {
            TextField(
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.focusRequester(searchFocusRequester).fillMaxSize(),
                value = searchText,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        webViewHolder.findNext()
                    }
                ),
                onValueChange = {
                    setSearchText(it)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Строка поиска"
                    )
                })
        }
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                SwipeRefresh(
                    state = scrollRefreshState,
                    modifier = Modifier.fillMaxSize(),
                    swipeEnabled = false,
                    indicatorPadding = PaddingValues(top = 80.dp),
                    onRefresh = {}
                ) {
                    WebViewComposable(
                        url = configuration.resolveUrl(page.name),
                        css = css,
                        visibilityFlag = if (loading) {
                            View.INVISIBLE
                        } else {
                            View.VISIBLE
                        },
                        onPageLoaded = {
                            setLoading(false)
                            upsertAsRecent()
                        },
                        onPageStartLoading = {
                            setLoading(true)
                        },
                        onWebViewCreated = { webViewHolder.setWebView(it) },
                        onScrollTop = { setButtonsBarVisible(true) },
                        onScrollBottom = { setButtonsBarVisible(false) }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .height(actionsBarHeightAnimated)
                .requiredHeight(actionsBarHeightAnimated)
        ) {
            Divider(modifier = Modifier.fillMaxWidth())
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { webViewHolder.goBack(navigateBack) }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { webViewHolder.reload() },
                        enabled = !loading,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Перезагрузить"
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            if (inFavorites) {
                                removeFromFavorites()
                            } else {
                                addToFavorites()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (inFavorites) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (inFavorites) "Удалить из избранного" else "Добавить в избранное"
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            val newSearchVisible = !searchVisible
                            setSearchVisible(newSearchVisible)
                            if (!newSearchVisible) {
                                setSearchText("")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (searchVisible) Icons.Filled.SearchOff else Icons.Filled.Search,
                            contentDescription = if (searchVisible) "Скрыть поиск" else "Показать поиск"
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { webViewHolder.goForward() }) {
                        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Вперед")
                    }
                }
            }
        }

    }
}