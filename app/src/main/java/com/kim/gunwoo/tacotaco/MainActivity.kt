package com.kim.gunwoo.tacotaco

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kim.gunwoo.tacotaco.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // 위치 권한 요청 등록
//        val launcher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            if (isGranted) {
//                // 권한이 허용되면 WorkManager 시작
//                startLocationWork()
//            } else {
//                Toast.makeText(this, "위치 권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // 위치 권한 확인
//        val status = ContextCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        if (status == PackageManager.PERMISSION_GRANTED) {
//            // 권한이 허용된 경우 WorkManager 시작
//            startLocationWork()
//        } else {
//            // 권한이 허용되지 않은 경우 권한을 요청
//            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//    // WorkManager를 사용해 LocationWorker를 주기적으로 실행
//    private fun startLocationWork() {
//        val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(15, TimeUnit.MINUTES)
//            .build()
//        WorkManager.getInstance(this).enqueue(workRequest)
//    }
    }
}
