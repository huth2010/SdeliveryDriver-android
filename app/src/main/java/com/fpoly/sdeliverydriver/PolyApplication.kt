package com.fpoly.sdeliverydriver

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.di.DaggerPolyComponent
import com.fpoly.sdeliverydriver.di.PolyComponent

class PolyApplication : Application() {
    companion object{
        const val CHANNEL_ID = "my_channel_01"
    }

    var currentUser: User? = null

    fun setUser(user: User?) {
        this.currentUser = user
    }

    val polyComponent: PolyComponent by lazy {
        DaggerPolyComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        polyComponent.inject(this)
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}