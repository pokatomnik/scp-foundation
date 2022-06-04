package tk.pokatomnik.scpfoundation.domain

data class PagedResponse(
    val documents: List<PageInfo>,
    val maxPage: Int
) {
    constructor() : this(listOf(), 1)
}