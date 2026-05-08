package com.depi.moviex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.depi.moviex.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    
    @Query("SELECT * FROM favorite_movies WHERE accountName = :accountName ORDER BY addedAt DESC")
    fun getAllFavoriteMovies(accountName: String): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE accountName = :accountName ORDER BY addedAt DESC")
    suspend fun getAllFavoriteMoviesOnce(accountName: String): List<FavoriteMovieEntity>
    
    @Query("SELECT * FROM favorite_movies WHERE accountName = :accountName AND category = :category ORDER BY addedAt DESC")
    fun getMoviesByCategory(accountName: String, category: String): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE accountName = :accountName AND category = :category ORDER BY addedAt DESC")
    suspend fun getMoviesByCategoryOnce(accountName: String, category: String): List<FavoriteMovieEntity>
    
    @Query("SELECT * FROM favorite_movies WHERE movieId = :movieId AND accountName = :accountName LIMIT 1")
    suspend fun getMovieById(movieId: Int, accountName: String): FavoriteMovieEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(movie: FavoriteMovieEntity)
    
    @Query("DELETE FROM favorite_movies WHERE movieId = :movieId AND accountName = :accountName")
    suspend fun removeFromFavorite(movieId: Int, accountName: String)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieId = :movieId AND accountName = :accountName LIMIT 1)")
    fun isInFavorite(movieId: Int, accountName: String): Flow<Boolean>
    
    @Query("SELECT DISTINCT category FROM favorite_movies WHERE accountName = :accountName ORDER BY category ASC")
    fun getCategories(accountName: String): Flow<List<String>>

    @Query("UPDATE favorite_movies SET accountName = :newAccountName WHERE accountName = :oldAccountName")
    suspend fun migrateAccount(oldAccountName: String, newAccountName: String)
}
