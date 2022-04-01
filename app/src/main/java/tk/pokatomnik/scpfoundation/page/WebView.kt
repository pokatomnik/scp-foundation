package tk.pokatomnik.scpfoundation.page

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

fun injectCSS(webView: WebView, css: String, onDone : (e : Error?) -> Unit) {
    val cssClean = css.trim().replace("\n", " ")

    if (cssClean == "") {
        onDone(null)
    }

    if (!webView.settings.javaScriptEnabled) {
        return onDone(Error("Failed to inject CSS because of disabled Javascript"))
    }

    val js = """
        var style = document.createElement('style');
        style.innerHTML = '$cssClean';
        document.head.appendChild(style);
    """.trimIndent()

    webView.evaluateJavascript(js) { onDone(null) }
}

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
    onWebViewCreated: (webView: WebView) -> Unit
) {
    val context = LocalContext.current

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

            webViewClient = SCPWebViewClient(
                onPageStartLoading = onPageStartLoading,
                onPageFailed = onPageFailed,
                onPageLoaded = {
                    injectCSS(this, css ?: "") { onPageLoaded() }
                }
            )

            onWebViewCreated(this)

            loadUrl(url)
        }
    }, update = {
        it.visibility = visibilityFlag
    })
}