package tk.pokatomnik.scpfoundation.page

import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun Page(
    url: String,
    navigateBack: () -> Unit
) {
    val webViewHolder = remember { WebViewHolder() }

    val (loading, setLoading) = remember { mutableStateOf(false) }

    val scrollRefreshState = rememberSwipeRefreshState(loading)

    val (buttonsBarVisible, setButtonsBarVisible) = remember { mutableStateOf(true) }
    val heightAnimated = remember { Animatable(if (buttonsBarVisible) 64f else 0f) }
    LaunchedEffect(buttonsBarVisible) {
        heightAnimated.animateTo(if (buttonsBarVisible) 64f else 0f)
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
                            url = url,
                            css = css,
                            visibilityFlag = if (loading) {
                                View.INVISIBLE
                            } else {
                                View.VISIBLE
                            },
                            onPageLoaded = {
                                setLoading(false)
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
            Row(
                modifier = Modifier
                    .height(heightAnimated.value.dp)
                    .requiredHeight(heightAnimated.value.dp),
            ) {
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
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Перезагрузить")
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