package tk.pokatomnik.scpfoundation.page

import android.view.View
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class PageState {
    INITIAL,
    LOADING,
    ERROR,
    DATA
}

fun pageStateToVisibility(pageState: PageState): Int {
    return when (pageState) {
        PageState.LOADING, PageState.ERROR, PageState.INITIAL -> View.GONE
        PageState.DATA -> View.VISIBLE
    }
}

class WebViewHolder() {
    private var _webView: WebView? = null

    fun setWebView(webView: WebView) {
        _webView = webView
    }

    fun goBack() {
        if (_webView?.canGoBack() == true) {
            _webView?.goBack()
        }
    }

    fun goForward() {
        if (_webView?.canGoForward() == true) {
            _webView?.goForward()
        }
    }
}

@Composable
fun Page(url: String?) {
    val (pageState, setPageState) = remember(url) {
        mutableStateOf(url?.let { PageState.INITIAL } ?: PageState.ERROR)
    }
    val webViewHolder = remember { WebViewHolder() }

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
                        visibilityFlag = pageStateToVisibility(pageState),
                        onPageLoaded = { setPageState(PageState.DATA) },
                        onPageFailed = { _, _ -> setPageState(PageState.ERROR) },
                        onPageStartLoading = { setPageState(PageState.LOADING) },
                        onWebViewCreated = { webViewHolder.setWebView(it) }
                    )
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
                IconButton(onClick = { webViewHolder.goBack() }) {
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