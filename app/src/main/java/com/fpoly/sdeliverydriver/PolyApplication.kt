package com.fpoly.sdeliverydriver

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import com.fpoly.sdeliverydriver.data.model.User
import com.fpoly.sdeliverydriver.di.DaggerPolyComponent
import com.fpoly.sdeliverydriver.di.PolyComponent
import com.fpoly.sdeliverydriver.ultis.MyConfigNotifi

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
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)

            val channelAll = NotificationChannel(MyConfigNotifi.CHANNEL_ID, "Thông báo tổng", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channelAll)

            val channelChat = NotificationChannel(MyConfigNotifi.CHANNEL_ID_CHAT, "Đoạn chat", NotificationManager.IMPORTANCE_HIGH)
            val uriSoundChat = Uri.parse("android.resource://" + packageName + "/" + R.raw.sound_messager)
            val audioAttr = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            channelChat.setSound(uriSoundChat, audioAttr)
            notificationManager.createNotificationChannel(channelChat)

            val channelCall = NotificationChannel(MyConfigNotifi.CHANNEL_ID_CALL, "Cuộc gọi", NotificationManager.IMPORTANCE_HIGH)
            channelChat.setSound(
                Uri.parse("android.resource://" + packageName + "/" + R.raw.sound_call),
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            )
            notificationManager.createNotificationChannel(channelCall)
        }
    }

}