package tk.pokatomnik.scpfoundation.di.db.dao.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tk.pokatomnik.scpfoundation.domain.PageInfo

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "url") override val url: String,
    @ColumnInfo(name = "rating") override val rating: Int?,
    @ColumnInfo(name = "author") override val author: String?,
    @ColumnInfo(name = "date") override val date: String?
) : PageInfo {
    constructor(pageInfo: PageInfo) : this(
        id = null,
        name = pageInfo.name,
        url = pageInfo.url,
        rating = pageInfo.rating,
        author = pageInfo.author,
        date = pageInfo.date
    )
}