package tk.pokatomnik.scpfoundation.page

import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class PageState {
    INITIAL,
    LOADING,
    ERROR,
    DATA;
    companion object {
        fun toVisibility(pageState: PageState): Int {
            return when (pageState) {
                LOADING, ERROR, INITIAL -> View.GONE
                DATA -> View.VISIBLE
            }
        }
    }
}

@Composable
fun Page(
    url: String?,
    navigateBack: () -> Unit
) {
    val (pageState, setPageState) = remember(url) {
        mutableStateOf(url?.let { PageState.INITIAL } ?: PageState.ERROR)
    }
    val webViewHolder = remember { WebViewHolder() }
    val (buttonsBarVisible, setButtonsBarVisible) = remember { mutableStateOf(true) }
    val heightAnimated = remember { Animatable(if (buttonsBarVisible) 64f else 0f) }
    LaunchedEffect(buttonsBarVisible) {
        heightAnimated.animateTo(if (buttonsBarVisible) 64f else 0f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                if (url == null || pageState == PageState.ERROR) {
                    // TODO handle error
                } else {
                    if (pageState == PageState.LOADING) {
                        CircularProgressIndicator()
                    }
                    WebViewComposable(
                        url = url,
                        css = css,
                        visibilityFlag = PageState.toVisibility(pageState),
                        onPageLoaded = { setPageState(PageState.DATA) },
                        onPageFailed = { _, _ -> setPageState(PageState.ERROR) },
                        onPageStartLoading = { setPageState(PageState.LOADING) },
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
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
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
                    Icon(Icons.Filled.ArrowForward, contentDescription = "Вперед")
                }
            }
        }
    }
}