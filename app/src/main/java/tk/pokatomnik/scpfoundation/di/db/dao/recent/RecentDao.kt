package tk.pokatomnik.scpfoundation.di.db.dao.recent

import androidx.room.*

@Dao
abstract class RecentDao {
    @Query("SELECT * FROM recent ORDER BY opened DESC")
    abstract suspend fun getAll(): List<Recent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(recent: Recent): Long

    @Query("UPDATE recent set opened = :opened WHERE url = :url")
    abstract suspend fun update(url: String, opened: Long)

    @Transaction
    open suspend fun upsert(recent: Recent) {
        val result = insert(recent)
        if (result == -1L) {
            update(recent.url, recent.opened)
        }
    }
}