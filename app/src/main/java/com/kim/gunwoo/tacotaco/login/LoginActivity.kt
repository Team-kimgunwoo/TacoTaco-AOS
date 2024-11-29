package com.kim.gunwoo.tacotaco.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kim.gunwoo.tacotaco.R
import com.kim.gunwoo.tacotaco.databinding.ActivityLoginBinding
import com.kim.gunwoo.tacotaco.databinding.ActivityMainBinding
import com.kim.gunwoo.tacotaco.emotion.EmotionActivity
import com.kim.gunwoo.tacotaco.server.remote.RetrofitBuilder
import com.kim.gunwoo.tacotaco.server.remote.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            if (binding.email.length() == 0 || binding.pw.length() == 0) {
                Log.d(TAG, "onCreate: 안됨")
            } else {
//
                fetchFcmToken()

                val intent = Intent(this@LoginActivity, EmotionActivity::class.java)
                startActivity(intent)



            }
        }

    }

    fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }

                // FCM 토큰
                val token = task.result
                Log.d("FCM", "FCM Token: $token")

                // 서버로 전송
                sendTokenToServer(token)
            }
    }


    private fun sendTokenToServer(token: String) {
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
}