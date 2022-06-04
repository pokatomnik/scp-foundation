package tk.pokatomnik.scpfoundation.di.db.dao.favorites

import androidx.room.*

@Dao
interface FavoritesDAO {
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorites WHERE name IN (:names)")
    suspend fun getByNames(names: Array<String>): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE name = :name)")
    suspend fun existsByName(name: String): Boolean
}