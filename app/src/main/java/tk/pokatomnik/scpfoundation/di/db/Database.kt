package tk.pokatomnik.scpfoundation.di.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.FavoritesDAO

@Database(
    entities = [Favorite::class],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun favoritesDAO(): FavoritesDAO
}