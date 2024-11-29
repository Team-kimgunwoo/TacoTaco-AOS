package com.kim.gunwoo.tacotaco.emotion

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kim.gunwoo.tacotaco.databinding.ActivityEmotionBinding
import com.kim.gunwoo.tacotaco.server.remote.RetrofitBuilder
import com.kim.gunwoo.tacotaco.server.remote.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kim.gunwoo.tacotaco.LocationWorker
import java.util.concurrent.TimeUnit

class EmotionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmotionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmotionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 위치 권한 요청 등록
        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // 권한이 허용되면 WorkManager 시작
                startLocationWork()
            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 위치 권한 확인
        val status = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (status == PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용된 경우 WorkManager 시작
            startLocationWork()
        } else {
            // 권한이 허용되지 않은 경우 권한을 요청
            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // 각 버튼에 대한 클릭 리스너 설정
        binding.buttonCallBaseball.setOnClickListener {
            send("BASEBALL")
        }
        binding.buttonCallStore.setOnClickListener {
            send("MART")
        }
        binding.buttonCallMartiallaw.setOnClickListener {
            send("WAR")
        }
        binding.buttonCallOuting.setOnClickListener {
            send("OUTING")
        }
        binding.buttonCallConsulting.setOnClickListener {
            send("COUNSEL")
        }
        binding.buttonCallAbsent.setOnClickListener {
            send("DROP")
        }
    }

    fun send(s: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                RetrofitBuilder.patchEmotionService().patchEmotion(
                    accessToken = "Bearer ${Url.assess}",
                    emotionType = s
                )
            }.onSuccess { result ->
                Log.d("성공?", "send: ${result.message}")
            }
        }
    }

    // WorkManager를 사용해 LocationWorker를 주기적으로 실행
    private fun startLocationWork() {
        val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}
