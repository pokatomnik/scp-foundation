package tk.pokatomnik.scpfoundation.domain

interface PagedResponse {
    val pages: List<PageInfo>
    val minPage: Int
    val maxPage: Int
}