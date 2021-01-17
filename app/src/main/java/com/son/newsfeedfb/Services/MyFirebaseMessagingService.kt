package com.son.newsfeedfb.Services
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.son.newsfeedfb.R
import com.son.newsfeedfb.TimeLine

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        if (p0.notification!=null){
            Log.e("TAG", "Message Notification Body: " + p0.notification!!.body)
            val title : String = p0.notification?.title!!
            val body : String = p0.notification?.body!!
            showNotification(this ,title, body)
        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
    private fun getCustomDesign(context: Context,title : String, message : String) : RemoteViews{
        val remoteViews= RemoteViews(context.applicationContext.packageName,R.layout.notification)
        remoteViews.setTextViewText(R.id.title,title)
        remoteViews.setTextViewText(R.id.message,message)
        remoteViews.setImageViewResource(R.id.icon,R.drawable.logo)
        return remoteViews
    }
     private fun showNotification(context: Context,title: String, body: String){
        val intent = Intent(context,TimeLine::class.java)
        val channelId = "notification_channel"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT)
        var builder = NotificationCompat.Builder(context.applicationContext,channelId)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setTimeoutAfter(2000)
        builder =
            builder.setContent(getCustomDesign(context,title,body))
        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId,"abc",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
}