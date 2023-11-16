package com.fpoly.sdeliverydriver.ui.notification

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.ui.chat.ChatActivity
import com.fpoly.sdeliverydriver.ultis.MyConfigNotifi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var notifi: RemoteMessage.Notification? = remoteMessage.notification

        val data: Map<String, String> = remoteMessage.getData()
        val type = data["type"]
        val idUrl = data["idUrl"]

        when(type){
            MyConfigNotifi.TYPE_ALL ->{

            }
            MyConfigNotifi.TYPE_CHAT ->{
                showNotifiChat("${notifi?.title}", "${notifi?.body}", type, idUrl)
            }
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCMService token", "onMessageReceived: $token");
    }

    private fun showNotifiChat(title: String, body: String, type: String?, idUrl: String?) {
        val notifiManager = NotificationManagerCompat.from(applicationContext)

        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString("type", type)
                putString("idUrl", idUrl) }
            )
        }

        val pendingIntent = TaskStackBuilder.create(this).run {
            addParentStack(ChatActivity::class.java)
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notfication = NotificationCompat.Builder(applicationContext, MyConfigNotifi.CHANNEL_ID_CHAT)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.logoapp)
            .setColor(resources.getColor(R.color.black, applicationContext.theme))
            .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.sound_messager))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notifiManager.notify(System.currentTimeMillis().toInt(), notfication.build())
        }
    }
}