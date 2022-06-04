package tk.pokatomnik.scpfoundation.features.configuration

import androidx.compose.runtime.compositionLocalOf
import tk.pokatomnik.scpfoundation.domain.Configuration
import tk.pokatomnik.scpfoundation.domain.UrlsConfiguration

internal val LocalConfiguration = compositionLocalOf {
    Configuration(
        urls = UrlsConfiguration(
            pageViewBaseUrl = ""
        )
    )
}