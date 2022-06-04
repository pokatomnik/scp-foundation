package tk.pokatomnik.scpfoundation.domain

data class PageByTags(
    val all_tags: List<String>,
    val id: String,
    val matchedTags: Int,
    val name: String,
    val title: String
) {
    fun toPage(): PageInfo {
        return PageInfo(
            name = name,
            title = title,
            rating = null,
            author = null,
            date = null
        )
    }
}