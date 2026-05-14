package com.depi.moviex.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.depi.moviex.receiver.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        movieId: Int,
        movieTitle: String,
        posterPath: String?,
        releaseDate: String,
        backdropPath: String? = null,
        overview: String? = null,
        voteAverage: Double = 0.0,
        voteCount: Int = 0,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("movieId", movieId)
            putExtra("movieTitle", movieTitle)
            putExtra("posterPath", posterPath)
            putExtra("releaseDate", releaseDate)
            putExtra("backdropPath", backdropPath)
            putExtra("overview", overview)
            putExtra("voteAverage", voteAverage)
            putExtra("voteCount", voteCount)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, movieId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = getTriggerTime(releaseDate)
        if (triggerTime > System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        }
    }

    fun cancelReminder(context: Context, movieId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, movieId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun getTriggerTime(releaseDate: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(releaseDate)
            if (date != null) {
                val calendar = Calendar.getInstance().apply {
                    time = date
                    set(Calendar.HOUR_OF_DAY, 10)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                calendar.timeInMillis
            } else {
                System.currentTimeMillis() + 60_000
            }
        } catch (e: Exception) {
            System.currentTimeMillis() + 60_000
        }
    }
}
