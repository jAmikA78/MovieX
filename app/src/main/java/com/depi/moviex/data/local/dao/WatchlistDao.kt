package com.depi.moviex.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.depi.moviex.data.local.entity.WatchlistMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    
    @Query("SELECT * FROM watchlist_movies ORDER BY addedAt DESC")
    fun getAllWatchlistMovies(): Flow<List<WatchlistMovieEntity>>
    
    @Query("SELECT * FROM watchlist_movies WHERE category = :category ORDER BY addedAt DESC")
    fun getMoviesByCategory(category: String): Flow<List<WatchlistMovieEntity>>
    
    @Query("SELECT * FROM watchlist_movies WHERE id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Int): WatchlistMovieEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movie: WatchlistMovieEntity)
    
    @Delete
    suspend fun removeFromWatchlist(movie: WatchlistMovieEntity)
    
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_movies WHERE id = :movieId LIMIT 1)")
    fun isInWatchlist(movieId: Int): Flow<Boolean>
    
    @Query("SELECT DISTINCT category FROM watchlist_movies ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>
}
