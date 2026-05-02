package com.depi.moviex.di

import android.content.Context
import androidx.room.Room
import com.depi.moviex.data.local.WatchlistDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): WatchlistDatabase {
        return Room.databaseBuilder(
            context,
            WatchlistDatabase::class.java,
            "moviex_database"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideWatchlistDao(database: WatchlistDatabase): com.depi.moviex.data.local.dao.WatchlistDao {
        return database.watchlistDao()
    }
}
