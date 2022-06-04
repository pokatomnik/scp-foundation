package tk.pokatomnik.scpfoundation.di.db

import androidx.room.*
import androidx.room.Database
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.Favorite
import tk.pokatomnik.scpfoundation.di.db.dao.favorites.FavoritesDAO
import tk.pokatomnik.scpfoundation.di.db.dao.recent.Recent
import tk.pokatomnik.scpfoundation.di.db.dao.recent.RecentDao

@Database(
    entities = [Favorite::class, Recent::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(
            from = 2,
            to = 3,
            spec = tk.pokatomnik.scpfoundation.di.db.Database.VercelBackendMigrationSpec::class
        )
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun favoritesDAO(): FavoritesDAO

    abstract fun recentDAO(): RecentDao

    @DeleteColumn(tableName = "favorites", columnName = "url")
    @DeleteColumn(tableName = "recent", columnName = "url")
    class VercelBackendMigrationSpec : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            super.onPostMigrate(db)
            db.execSQL("DELETE FROM favorites")
            db.execSQL("DELETE from recent")
        }
    }
}