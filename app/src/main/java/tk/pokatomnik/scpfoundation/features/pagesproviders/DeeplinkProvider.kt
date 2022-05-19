package tk.pokatomnik.scpfoundation.features.pagesproviders

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun DeeplinkProvider(
    uri: Uri?,
    children: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalDeeplink provides Deeplink(uri)
    ) {
        children()
    }
}