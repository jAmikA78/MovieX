package com.depi.moviex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.depi.moviex.data.local.dao.FavoriteDao
import com.depi.moviex.data.local.dao.ReminderDao
import com.depi.moviex.data.local.entity.FavoriteMovieEntity
import com.depi.moviex.data.local.entity.ReminderEntity

@Database(
    entities = [FavoriteMovieEntity::class, ReminderEntity::class],
    version = 4,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun reminderDao(): ReminderDao
}
