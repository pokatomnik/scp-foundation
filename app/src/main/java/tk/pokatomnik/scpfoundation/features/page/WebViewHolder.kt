package tk.pokatomnik.scpfoundation.features.page

import android.webkit.WebView

internal class WebViewHolder {
    private var _webView: WebView? = null

    fun setWebView(webView: WebView) {
        _webView = webView
    }

    fun goBack(navigateBack: () -> Unit) {
        if (_webView?.canGoBack() == true) {
            _webView?.goBack()
        } else {
            navigateBack()
        }
    }

    fun goForward() {
        if (_webView?.canGoForward() == true) {
            _webView?.goForward()
        }
    }

    fun reload() {
        _webView?.reload()
    }
}