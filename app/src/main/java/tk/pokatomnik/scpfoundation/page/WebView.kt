package tk.pokatomnik.scpfoundation.page

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class BooleanHolder(var value: Boolean)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComposable(
    css: String?,
    textZoom: Int = 150,
    url: String,
    visibilityFlag: Int,
    onPageStartLoading: () -> Unit,
    onPageLoaded: () -> Unit,
    onPageFailed: (
        request: WebResourceRequest?,
        error: WebResourceError?
    ) -> Unit,
    onWebViewCreated: (webView: WebView) -> Unit,
    onScrollTop: () -> Unit = {},
    onScrollBottom: () -> Unit = {}
) {
    val touchTracker = remember { BooleanHolder(false) }

    val context = LocalContext.current

    val coroutineScope = remember { CoroutineScope(Job() + Dispatchers.IO) }

    val scrollHolder = remember { ScrollHolder(coroutineScope) }

    AndroidView(factory = {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            visibility = visibilityFlag
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.textZoom = textZoom

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> touchTracker.value = true
                    MotionEvent.ACTION_UP -> touchTracker.value = false
                }
                view.performClick()
                false
            }

            webViewClient = SCPWebViewClient(
                onPageStartLoading = onPageStartLoading,
                onPageFailed = onPageFailed,
                onPageLoaded = {
                    injectCSS(this, css ?: "") { onPageLoaded() }
                }
            )

            scrollHolder.init(
                initialValue = scrollY,
                onScrollBottom = onScrollBottom,
                onScrollTop = onScrollTop,
            )

            setOnScrollChangeListener {_, _, _, _, _ ->
                if (touchTracker.value) {
                    scrollHolder.set(scrollY)
                }
            }

            onWebViewCreated(this)

            loadUrl(url)
        }
    }, update = {
        it.visibility = visibilityFlag
    })
}