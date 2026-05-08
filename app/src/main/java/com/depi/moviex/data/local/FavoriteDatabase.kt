package com.depi.moviex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.depi.moviex.data.local.dao.FavoriteDao
import com.depi.moviex.data.local.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 3,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
