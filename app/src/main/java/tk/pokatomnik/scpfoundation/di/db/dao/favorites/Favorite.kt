package tk.pokatomnik.scpfoundation.di.db.dao.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tk.pokatomnik.scpfoundation.domain.PageInfo

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "title", defaultValue = "") val title: String,
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "date") val date: String?
) {
    constructor(pageInfo: PageInfo) : this(
        id = null,
        name = pageInfo.name,
        title = pageInfo.title,
        rating = pageInfo.rating,
        author = pageInfo.author,
        date = pageInfo.date
    )
}