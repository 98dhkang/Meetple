package com.project.meetple

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val job by lazy { Job() }
    private val chatDAO by lazy { AppDB.getDatabase(applicationContext).ChatDAO()}

    private val TAG = "FirebaseService"

    // 토큰이 새로 발행되면 SharedPreferences에 저장 -> MainActivity에서 서버DB로 전송
    override fun onNewToken(token: String?) {
        setString(this,"token",token)
    }

    // 푸쉬알림 받았을 때 처리
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("fuck","ㅇㅇ")

        // Notifiaction 실행
        if(remoteMessage.getData().size > 0) {
            val handler = Handler(Looper.getMainLooper())
            if(remoteMessage.data.get("type")=="1") {
                val userName = remoteMessage.data.get("userName")!!
                val content = remoteMessage.data.get("content")!!
                sendNotification(userName, content)
            }
            else if(remoteMessage.data.get("type")=="2"){
                val roomName = remoteMessage.data.get("roomName")!!
                val title = "미팅방(" + remoteMessage.data.get("title") +")"
                val time = remoteMessage.data.get("last_msg_time")!!
                createChatRoom(roomName, title, time)
            }
        }
    }

    // Notification 처리
    private fun sendNotification(userName: String?, content: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", "")
        }

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder = NotificationCompat.Builder(this,"Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(userName)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)
            .setChannelId("mmetPleNChannel")

        var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun createChatRoom(roomName: String, title: String, time: String){
        val chatRoom : chat_room_db = chat_room_db(0, roomName, title, "", time)
        chatDAO.insertChatRoom(chatRoom)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", "")
        }

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder = NotificationCompat.Builder(this,"Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("밋플")
            .setContentText("미팅방이 개설됐어요!")
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)
            .setChannelId("mmetPleNChannel")

        var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}