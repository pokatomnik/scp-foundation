package tk.pokatomnik.scpfoundation.page

import android.webkit.WebView

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