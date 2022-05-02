package tk.pokatomnik.scpfoundation.features.page

import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import tk.pokatomnik.scpfoundation.di.db.dao.recent.Recent
import tk.pokatomnik.scpfoundation.di.db.rememberDatabase
import tk.pokatomnik.scpfoundation.domain.PageInfoImpl
import java.util.*

@Composable
fun Page(
    page: PageInfoImpl?,
    navigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val database = rememberDatabase()
    val webViewHolder = remember { WebViewHolder() }

    val (loading, setLoading) = remember { mutableStateOf(false) }
    val (inFavorites, setInFavorites) = remember { mutableStateOf(false) }

    val scrollRefreshState = rememberSwipeRefreshState(loading)

    val (buttonsBarVisible, setButtonsBarVisible) = remember { mutableStateOf(true) }
    val heightAnimated = remember { Animatable(if (buttonsBarVisible) 64f else 0f) }

    LaunchedEffect(Unit) {
        page?.let { page ->
            scope.launch {
                val exists = database.favoritesDAO().existsByUrl(page.url)
                setInFavorites(exists)
            }
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
                database.favoritesDAO().deleteByURL(page.url)
            }
        }
    }

    LaunchedEffect(buttonsBarVisible) {
        heightAnimated.animateTo(if (buttonsBarVisible) 64f else 0f)
    }

    if (page == null) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                        url = page.url,
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
                .height(heightAnimated.value.dp)
                .requiredHeight(heightAnimated.value.dp)
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
                    IconButton(onClick = { webViewHolder.goForward() }) {
                        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Вперед")
                    }
                }
            }
        }

    }
}