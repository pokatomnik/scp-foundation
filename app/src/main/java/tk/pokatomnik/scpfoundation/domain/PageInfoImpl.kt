package tk.pokatomnik.scpfoundation.domain

class PageInfoImpl(
    override val name: String,
    override val url: String,
    override val rating: Int?,
    override val author: String?,
    override val date: String?
) : PageInfo