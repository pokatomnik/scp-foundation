package tk.pokatomnik.scpfoundation.domain

import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.dao.recent.Recent
import java.util.*

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

    constructor(recent: Recent) : this(
        name = recent.name,
        url = recent.url,
        rating = recent.rating,
        author = recent.author,
        date = recent.date,
    )

    fun toFavorite(): Favorite {
        return Favorite(
            id = null,
            name = name,
            url = url,
            rating = rating,
            author = author,
            date = date,
        )
    }

    fun toRecent(): Recent {
        return Recent(
            name = name,
            url = url,
            rating = rating,
            author = author,
            date = date,
            opened = Calendar.getInstance().time.time
        )
    }
}