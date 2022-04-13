package tk.pokatomnik.scpfoundation.domain

import tk.pokatomnik.scpfoundation.di.http.HttpClient

class PageByTagsImpl(
    val id: String,
    val name: String,
    val matchedTags: Int,
    val title: String
) {
    fun toPage(): PageInfo {
        return PageInfoImpl(
            name = title,
            url = "${HttpClient.WEBSITE_URL}${name}",
            rating = null,
            author = null,
            date = null
        )
    }
}