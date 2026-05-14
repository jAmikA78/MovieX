package com.depi.moviex.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import com.depi.moviex.utils.K
import com.depi.moviex.utils.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val movieId = intent.getIntExtra("movieId", 0)
        val movieTitle = intent.getStringExtra("movieTitle") ?: return
        val posterPath = intent.getStringExtra("posterPath")
        val releaseDate = intent.getStringExtra("releaseDate") ?: ""
        val backdropPath = intent.getStringExtra("backdropPath")
        val overview = intent.getStringExtra("overview")
        val voteAverage = intent.getDoubleExtra("voteAverage", 0.0)
        val voteCount = intent.getIntExtra("voteCount", 0)

        CoroutineScope(Dispatchers.IO).launch {
            val posterBitmap = try {
                posterPath?.let {
                    val url = URL("${K.BASE_IMAGE_URL}$it")
                    BitmapFactory.decodeStream(url.openConnection().getInputStream())
                }
            } catch (e: Exception) {
                null
            }

            NotificationHelper.showMovieReminderNotification(
                context = context,
                movieId = movieId,
                movieTitle = movieTitle,
                posterPath = posterPath,
                releaseDate = releaseDate,
                backdropPath = backdropPath,
                overview = overview,
                voteAverage = voteAverage,
                voteCount = voteCount,
                posterBitmap = posterBitmap,
            )
        }
    }
}
