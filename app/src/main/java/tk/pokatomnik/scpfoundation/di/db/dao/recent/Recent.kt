package tk.pokatomnik.scpfoundation.di.db.dao.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tk.pokatomnik.scpfoundation.domain.PageInfo

@Entity(tableName = "recent")
data class Recent (
    @ColumnInfo(name = "name") val name: String,
    @PrimaryKey @ColumnInfo(name = "title", defaultValue = "") val title: String,
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "opened") val opened: Long
) {
    constructor(pageInfo: PageInfo, opened: Long) : this(
        name = pageInfo.name,
        title = pageInfo.title,
        rating = pageInfo.rating,
        author = pageInfo.author,
        date = pageInfo.date,
        opened = opened,
    )
}