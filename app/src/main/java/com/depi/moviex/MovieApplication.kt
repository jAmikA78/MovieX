package com.depi.moviex

import android.app.Application
import com.depi.moviex.utils.NotificationHelper
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        NotificationHelper.createNotificationChannel(this)
    }
}
