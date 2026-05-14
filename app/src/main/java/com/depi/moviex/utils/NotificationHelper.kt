package com.depi.moviex.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.depi.moviex.MainActivity
import com.depi.moviex.R
import com.depi.moviex.receiver.FavoriteActionReceiver

object NotificationHelper {

    const val CHANNEL_ID = "movie_reminders"
    const val CHANNEL_NAME = "Movie Reminders"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminders for upcoming movie releases"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showMovieReminderNotification(
        context: Context,
        movieId: Int,
        movieTitle: String,
        posterPath: String?,
        releaseDate: String,
        backdropPath: String? = null,
        overview: String? = null,
        voteAverage: Double = 0.0,
        voteCount: Int = 0,
        posterBitmap: Bitmap? = null,
    ) {
        val detailIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "movie_detail/movie/$movieId")
        }
        val detailPendingIntent = PendingIntent.getActivity(
            context, movieId, detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val favIntent = Intent(context, FavoriteActionReceiver::class.java).apply {
            putExtra("movieId", movieId)
            putExtra("movieTitle", movieTitle)
            putExtra("posterPath", posterPath)
            putExtra("backdropPath", backdropPath)
            putExtra("releaseDate", releaseDate)
            putExtra("overview", overview)
            putExtra("voteAverage", voteAverage)
            putExtra("voteCount", voteCount)
        }
        val favPendingIntent = PendingIntent.getBroadcast(
            context, movieId + 1000, favIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(movieTitle)
            .setContentText(context.getString(R.string.released_today))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(detailPendingIntent)
            .setAutoCancel(true)

        if (posterBitmap != null) {
            val style = NotificationCompat.BigPictureStyle()
                .bigPicture(posterBitmap)
                .bigLargeIcon(null as Bitmap?)
                .setBigContentTitle(movieTitle)
                .setSummaryText(context.getString(R.string.released_today_date, releaseDate))
            builder.setStyle(style)
        }

        builder.addAction(
            android.R.drawable.star_on,
            context.getString(R.string.add_to_fav),
            favPendingIntent
        )
        builder.addAction(
            android.R.drawable.ic_input_add,
            context.getString(R.string.watch_details),
            detailPendingIntent
        )

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(movieId, builder.build())
    }
}
