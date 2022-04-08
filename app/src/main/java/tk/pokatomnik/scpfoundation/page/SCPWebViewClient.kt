package tk.pokatomnik.scpfoundation.page

import android.graphics.Bitmap
import android.webkit.*

class SCPWebViewClient(
    private val onPageLoaded: (() -> Unit)?,
    private val onPageFailed: ((
        request: WebResourceRequest?,
        error: WebResourceError?
    ) -> Unit)?,
    private val onPageStartLoading: (() -> Unit)?
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        onPageStartLoading?.invoke()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onPageLoaded?.invoke()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        onPageFailed?.invoke(request, error)
        view?.apply {
            loadUrl("about:blank")
            loadDataWithBaseURL(null, ERROR_HTML, "text/html", "UTF-8", null)
            invalidate()
        }
    }
}