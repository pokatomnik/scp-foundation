package tk.pokatomnik.scpfoundation.features.pagesproviders

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf

internal class Deeplink(
    val uri: Uri?
)

internal val LocalDeeplink = compositionLocalOf {
    Deeplink(null)
}
