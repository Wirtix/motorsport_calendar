package com.example.motorsportcalendar.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.motorsportcalendar.MainActivity
import com.example.motorsportcalendar.R
import com.example.motorsportcalendar.data.model.F1
import kotlin.random.Random

class NotificationHandler(private val context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"


    // SIMPLE NOTIFICATION
    fun showSimpleNotification(race:F1, howMuchBefore:String) {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("${race.category} Race: ${race.name}")
            .setContentText(howMuchBefore)
            .setSmallIcon(R.drawable.racing_helmet)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}