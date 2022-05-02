package tk.pokatomnik.scpfoundation.di.db.dao.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tk.pokatomnik.scpfoundation.domain.PageInfo

@Entity(tableName = "recent")
data class Recent (
    @ColumnInfo(name = "name") override val name: String,
    @PrimaryKey @ColumnInfo(name = "url") override val url: String,
    @ColumnInfo(name = "rating") override val rating: Int?,
    @ColumnInfo(name = "author") override val author: String?,
    @ColumnInfo(name = "date") override val date: String?,
    @ColumnInfo(name = "opened") val opened: Long
) : PageInfo {
    constructor(pageInfo: PageInfo, opened: Long) : this(
        name = pageInfo.name,
        url = pageInfo.url,
        rating = pageInfo.rating,
        author = pageInfo.author,
        date = pageInfo.date,
        opened = opened,
    )
}