package com.app.amtcust.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.amtcust.R
import com.app.amtcust.activity.*
import com.app.amtcust.utils.convertDateToString
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        var title = ""
        var msg = ""
        var id = ""
        var type = ""

        if (remoteMessage!!.getNotification() == null) {
            return
        }
        if(remoteMessage!!.getNotification() != null) {

            Log.e(TAG, "remoteMessage notification body: " + remoteMessage.notification!!.body)
            Log.e(TAG, "remoteMessage notification title: " + remoteMessage.notification!!.title)
            title = remoteMessage!!.notification!!.title.toString()
            msg = remoteMessage!!.notification!!.body.toString()
        }

        Log.e(TAG, "Message data payload: " + remoteMessage.data)

        if(remoteMessage.data.isEmpty()) {
            sendNotificationFCM(title, msg,"clickAction")
        } else {
            remoteMessage.data.isNotEmpty().let {

                Log.e(TAG, "Message data payload: " + remoteMessage.data)

                id = remoteMessage.data["ID"]!!
                type = remoteMessage.data["NotificationType"]!!

            }
            sendNotification(title, msg, id, type, "clickAction")
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
    }

    private fun sendNotification(title: String, messageBody: String, id:String, type:String, clickAction: String) {

        var intent: Intent? = null

        if(type.equals("HOTELVOUCHER")) {
            intent = Intent(this, MyVoucherListActivity::class.java)
            intent.putExtra("ID",id.toInt())
            intent.putExtra("NotificationType",type)
        }
        else if(type.equals("AIRLINEVOUCHER")) {
            intent = Intent(this, FlightVoucherDetailActivity::class.java)
            intent.putExtra("ID",id.toInt())
            intent.putExtra("NotificationType",type)
        }
        else if(type.equals("ROUTEVOUCHER")) {
            intent = Intent(this, RouteVoucherDetailsActivity::class.java)
            intent.putExtra("ID",id.toInt())
            intent.putExtra("NotificationType",type)
        }
        else if(type.equals("PAYMENTRECEIPT")) {
            intent = Intent(this, PaymentReceiptDetailsActivity::class.java)
            intent.putExtra("ID",id.toInt())
            intent.putExtra("NotificationType",type)
        }
        else if(type.equals("CANCELLATIONBOOKING")) {
            intent = Intent(this, TourBookingFormActivity::class.java)
            intent.putExtra("ID",id.toInt())
            intent.putExtra("NotificationType",type)
        }
        else {
            intent = Intent(this, SplashActivity::class.java)
            intent.putExtra("ID",id)
            intent.putExtra("NotificationType",type)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)

         var smallIcon = R.mipmap.logo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smallIcon = R.mipmap.logo
        } else {
            smallIcon = R.mipmap.logo
        }

        val channelId = getString(R.string.notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title) // getString(R.string.app_name)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setPriority(NotificationCompat.PRIORITY_LOW)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(createID() /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    private fun createID(): Int {
        val dateNow = Date()
        val id = convertDateToString(dateNow, "ddHHmmsss")
        Log.i(TAG, "-------messageBody - createID==>${id.toInt()}")
        return id.toInt()
    }

    private fun sendNotificationFCM(title: String, messageBody: String, clickAction: String) {

        var smallIcon = R.mipmap.logo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smallIcon = R.mipmap.logo
        } else {
            smallIcon = R.mipmap.logo
        }

        val channelId = getString(R.string.notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val defaultSoundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.noti2)
        val uri = Uri.parse("android.resource://$packageName/raw/noti2")
        val defaults = Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title) // getString(R.string.app_name)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(uri, AudioManager.STREAM_NOTIFICATION)
//            .setDefaults(defaults)
            .setContentIntent(null)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setPriority(NotificationCompat.PRIORITY_LOW)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(createID() /* ID of notification */, notificationBuilder.build())
    }

}
