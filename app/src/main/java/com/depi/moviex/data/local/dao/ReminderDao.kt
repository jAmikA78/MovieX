package com.depi.moviex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.depi.moviex.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder_movies WHERE accountName = :accountName ORDER BY releaseDate ASC")
    fun getAllReminders(accountName: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder_movies WHERE accountName = :accountName ORDER BY releaseDate ASC")
    suspend fun getAllRemindersOnce(accountName: String): List<ReminderEntity>

    @Query("SELECT * FROM reminder_movies WHERE movieId = :movieId AND accountName = :accountName LIMIT 1")
    suspend fun getReminder(movieId: Int, accountName: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminder_movies WHERE movieId = :movieId AND accountName = :accountName")
    suspend fun removeReminder(movieId: Int, accountName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM reminder_movies WHERE movieId = :movieId AND accountName = :accountName LIMIT 1)")
    fun isReminderSet(movieId: Int, accountName: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM reminder_movies WHERE movieId = :movieId AND accountName = :accountName LIMIT 1)")
    suspend fun isReminderSetOnce(movieId: Int, accountName: String): Boolean
}
