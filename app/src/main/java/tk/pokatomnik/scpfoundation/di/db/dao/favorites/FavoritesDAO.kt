package tk.pokatomnik.scpfoundation.di.db.dao.favorites

import androidx.room.*

@Dao
interface FavoritesDAO {
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorites WHERE url IN (:urls)")
    suspend fun getByURLs(urls: Array<String>): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE url = :url")
    suspend fun deleteByURL(url: String)
}