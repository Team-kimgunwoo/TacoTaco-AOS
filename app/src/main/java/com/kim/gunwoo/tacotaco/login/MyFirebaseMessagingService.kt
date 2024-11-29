package com.kim.gunwoo.tacotaco.login

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kim.gunwoo.tacotaco.server.remote.RetrofitBuilder
import com.kim.gunwoo.tacotaco.server.remote.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "default_channel"
    private val notificationId = 1

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        // 새 토큰을 서버로 전송
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 메시지를 받았을 때의 로직
        Log.d("FCM", "From: ${remoteMessage.from}")

        // 메시지가 Notification 유형일 경우
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Title: ${it.title}")
            Log.d("FCM", "Message Notification Body: ${it.body}")
            // 여기서 알림을 표시하는 로직 추가 가능
            showNotification(it.body)

        }
    }

    private fun showNotification(messageBody: String?) {
        // NotificationManager 생성
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 채널이 없으면 생성 (Android 8.0 이상에서 필수)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("새로운 알림")
            .setContentText(messageBody)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // 작은 아이콘
            .build()

        // 알림 표시
        notificationManager.notify(notificationId, notification)
    }

    private fun sendTokenToServer(token: String) {
        // 서버로 FCM 토큰 전송
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                // Retrofit을 이용해 서버에 토큰 전송
                RetrofitBuilder.postFcmService().postFcm(
                    accessToken = "Bearer ${Url.assess}",
                    fcmToken = token
                )
            }.onSuccess {
                Log.d("FCM", "Token sent to server successfully")
            }.onFailure {
                Log.e("FCM", "Failed to send token to server", it)
            }
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        // 수신된 데이터 메시지를 처리하는 로직
        Log.d("FCM", "Handling Data Message: $data")
        // 예: 특정 키를 기반으로 액션 수행
        val action = data["action"]
        when (action) {
            "open_screen" -> {
                val screenName = data["screen_name"]
                Log.d("FCM", "Open screen: $screenName")
                // 화면 이동 로직 추가 가능
            }
            "show_alert" -> {
                val message = data["message"]
                Log.d("FCM", "Show alert: $message")
                // AlertDialog 표시 로직 추가 가능
            }
            else -> Log.d("FCM", "Unknown action")
        }
    }
}
