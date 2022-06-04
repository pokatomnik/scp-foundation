package tk.pokatomnik.scpfoundation.domain

import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.dao.recent.Recent
import java.util.*

data class PageInfo(
    /**
     * Name of the document.
     * Example: scp-173
     */
    val name: String,
    /**
     * Human-readable title.
     * Example: SCP-173 - Скульптура
     */
    val title: String,
    /**
     * Rating of this document. Optional.
     */
    val rating: Int?,
    /**
     * Author of this document. Optional.
     */
    val author: String?,
    /**
     * Date this document been created. Optional.
     */
    val date: String?
) {
    constructor(favorite: Favorite) : this(
        name = favorite.name,
        title = favorite.title,
        rating = favorite.rating,
        author = favorite.author,
        date = favorite.date
    )

    constructor(recent: Recent) : this(
        name = recent.name,
        title = recent.title,
        rating = recent.rating,
        author = recent.author,
        date = recent.date,
    )

    fun toFavorite(): Favorite {
        return Favorite(
            id = null,
            name = name,
            title = title,
            rating = rating,
            author = author,
            date = date,
        )
    }

    fun toRecent(): Recent {
        return Recent(
            name = name,
            title = title,
            rating = rating,
            author = author,
            date = date,
            opened = Calendar.getInstance().time.time
        )
    }
}