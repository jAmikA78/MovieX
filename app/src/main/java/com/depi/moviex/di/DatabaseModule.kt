package com.depi.moviex.di

import android.content.Context
import androidx.room.Room
import com.depi.moviex.data.local.FavoriteDatabase
import com.depi.moviex.data.local.dao.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "moviex_database"
        ).fallbackToDestructiveMigration().build()
    }
    
    @Provides
    @Singleton
    fun provideFavoriteDao(database: FavoriteDatabase): com.depi.moviex.data.local.dao.FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideReminderDao(database: FavoriteDatabase): ReminderDao {
        return database.reminderDao()
    }
}
