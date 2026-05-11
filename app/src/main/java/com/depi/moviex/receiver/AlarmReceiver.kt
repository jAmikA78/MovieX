package com.depi.moviex.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.depi.moviex.utils.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val movieId = intent.getIntExtra("movieId", 0)
        val movieTitle = intent.getStringExtra("movieTitle") ?: return
        val posterPath = intent.getStringExtra("posterPath")
        val releaseDate = intent.getStringExtra("releaseDate") ?: ""

        NotificationHelper.showMovieReminderNotification(
            context = context,
            movieId = movieId,
            movieTitle = movieTitle,
            posterPath = posterPath,
            releaseDate = releaseDate
        )
    }
}
