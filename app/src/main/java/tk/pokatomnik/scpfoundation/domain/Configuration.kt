package tk.pokatomnik.scpfoundation.domain

import tk.pokatomnik.scpfoundation.utils.joinURLParts

data class UrlsConfiguration(val pageViewBaseUrl: String)

data class Configuration(val urls: UrlsConfiguration) {
    fun resolveUrl(name: String): String {
        return joinURLParts(urls.pageViewBaseUrl, name)
    }
}