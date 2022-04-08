package tk.pokatomnik.scpfoundation.domain

class PagedResponseImpl(
    override val pages: List<PageInfo>,
    override val minPage: Int,
    override val maxPage: Int
) : PagedResponse {
    constructor() : this(
        pages = listOf(),
        minPage = 1,
        maxPage = 1
    )
}