package tk.pokatomnik.scpfoundation.di.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.FavoritesDAO
import tk.pokatomnik.scpfoundation.di.db.dao.recent.Recent
import tk.pokatomnik.scpfoundation.di.db.dao.recent.RecentDao

@Database(
    entities = [Favorite::class, Recent::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun favoritesDAO(): FavoritesDAO

    abstract fun recentDAO(): RecentDao
}