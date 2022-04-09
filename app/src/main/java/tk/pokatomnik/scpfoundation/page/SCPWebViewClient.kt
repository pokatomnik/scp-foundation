package tk.pokatomnik.scpfoundation.page

import android.graphics.Bitmap
import android.webkit.*

class SCPWebViewClient(
    private val onLoadStart: (() -> Unit)?,
    private val onLoadEnd: (() -> Unit)?,
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        onLoadStart?.invoke()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onLoadEnd?.invoke()
    }
}