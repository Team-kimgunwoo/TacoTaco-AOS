package com.kim.gunwoo.tacotaco

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kim.gunwoo.tacotaco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var resultView: TextView
    private lateinit var providerClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultView = binding.resultView
        providerClient = LocationServices.getFusedLocationProviderClient(this)

        // 권한 요청 결과를 처리하기 위한 등록
        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // 권한이 허용된 경우 위치 정보를 가져옴
                getLastLocation()
            } else {
                // 권한이 거부된 경우 메시지를 표시
                Toast.makeText(this, "위치 권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 위치 권한이 허용되었는지 확인
        val status = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (status == PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용된 경우 위치 정보를 가져옴
            getLastLocation()
        } else {
            // 권한이 허용되지 않은 경우 권한을 요청
            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // 마지막으로 알려진 위치 정보를 가져오는 메서드
    private fun getLastLocation() {
        try {
            providerClient.lastLocation.addOnSuccessListener { location ->
                val latitude = location?.latitude
                val longitude = location?.longitude
                if (latitude != null && longitude != null) {
                    resultView.text = "$latitude, $longitude"
                } else {
                    resultView.text = "위치 정보를 가져올 수 없습니다"
                }
            }
        } catch (e: SecurityException) {
            // 위치 접근이 거부되었을 때 예외를 처리하고 메시지를 표시
            Toast.makeText(this, "위치 접근이 거부되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}