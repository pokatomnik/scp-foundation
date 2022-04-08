package tk.pokatomnik.scpfoundation.domain

import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite

class PageInfoImpl(
    override val name: String,
    override val url: String,
    override val rating: Int?,
    override val author: String?,
    override val date: String?
) : PageInfo {
    constructor(favorite: Favorite) : this(
        name = favorite.name,
        url = favorite.url,
        rating = favorite.rating,
        author = favorite.author,
        date = favorite.date
    )
}