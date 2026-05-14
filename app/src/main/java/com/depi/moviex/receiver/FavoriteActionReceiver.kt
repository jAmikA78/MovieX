package com.depi.moviex.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.depi.moviex.R
import com.depi.moviex.auth.domain.repository.AuthRepository
import com.depi.moviex.data.local.dao.FavoriteDao
import com.depi.moviex.data.local.entity.FavoriteMovieEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteActionReceiver : BroadcastReceiver() {

    @Inject lateinit var favoriteDao: FavoriteDao
    @Inject lateinit var authRepository: AuthRepository

    override fun onReceive(context: Context, intent: Intent) {
        val movieId = intent.getIntExtra("movieId", 0)
        val title = intent.getStringExtra("movieTitle") ?: return
        val posterPath = intent.getStringExtra("posterPath")
        val backdropPath = intent.getStringExtra("backdropPath")
        val releaseDate = intent.getStringExtra("releaseDate") ?: ""
        val overview = intent.getStringExtra("overview") ?: ""
        val voteAverage = intent.getDoubleExtra("voteAverage", 0.0)
        val voteCount = intent.getIntExtra("voteCount", 0)

        val accountName = authRepository.getAccountName()
        val entity = FavoriteMovieEntity(
            movieId = movieId,
            accountName = accountName,
            title = title,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            overview = overview,
            category = "reminder"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                favoriteDao.addToFavorite(entity)
            } catch (_: Exception) { }
        }
        Toast.makeText(context, context.getString(R.string.added_to_favorites, title), Toast.LENGTH_SHORT).show()
    }
}
