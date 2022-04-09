package tk.pokatomnik.scpfoundation.page

import android.webkit.WebView

internal fun injectCSS(webView: WebView, css: String, onDone : (e : Error?) -> Unit) {
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