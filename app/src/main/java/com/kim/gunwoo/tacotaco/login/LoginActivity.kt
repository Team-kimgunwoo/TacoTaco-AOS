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
import com.kim.gunwoo.tacotaco.server.local.TacotacoDB
import com.kim.gunwoo.tacotaco.server.local.TokenEntity
import com.kim.gunwoo.tacotaco.server.remote.RetrofitBuilder
import com.kim.gunwoo.tacotaco.server.remote.request.login.LoginRequest
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
                login(
                    emailText = binding.email.text.toString(),
                    passwordText = binding.pw.text.toString()
                )
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful){
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            token = task.result
            Log.d("Login FCM", "onCreate: ${token}")
        })

    }

    private fun login(emailText: String, passwordText: String) {

        var accessToken = ""
        var refreshToken = ""

        lifecycleScope.launch(Dispatchers.IO){
            kotlin.runCatching {
                RetrofitBuilder.getLoginService()
                    .postLogin(LoginRequest(email = emailText, pw = passwordText, fcmToken = token))
            }.onSuccess { result->
                accessToken = result.data?.accessToken ?: ""
                refreshToken = result.data?.refreshToken ?: ""
                lifecycleScope.launch(Dispatchers.Main) {
                    saveToken(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        emailText = emailText
                    )
//                    moveScreen()
                    Log.d(TAG, "login: 성공")
                    val intent = Intent(this@LoginActivity, EmotionActivity::class.java)
                    startActivity(intent)
                }
            }.onFailure { 
                it.printStackTrace()
                Log.d(TAG, "login: 실패")
                Log.d(TAG, "login: ${it.message}")
            }
        }
    }

//    private fun moveScreen() {
//        startActivity(Intent(this, HomeActivity::class.java))
//    }

    private fun saveToken(accessToken: String, refreshToken: String, emailText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            TacotacoDB.getInstance(this@LoginActivity)?.tokenDao()?.insertMember(
                TokenEntity(
                    id = 1,
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        }
    }
}