package com.depi.moviex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.depi.moviex.data.local.dao.WatchlistDao
import com.depi.moviex.data.local.entity.WatchlistMovieEntity

@Database(
    entities = [WatchlistMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}
