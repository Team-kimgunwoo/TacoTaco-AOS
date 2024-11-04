package com.kim.gunwoo.tacotaco

import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun doWork(): Result {
        // 위치 정보 가져오기
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val latitude = location?.latitude
                val longitude = location?.longitude

                if (latitude != null && longitude != null) {
                    // 위치 정보를 사용할 수 있음 (예: 로그 출력, 데이터베이스 저장, 서버 전송)
                    Toast.makeText(applicationContext, "위치: $latitude, $longitude", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            // 위치 접근 권한이 없는 경우 예외 처리
            Toast.makeText(applicationContext, "위치 접근이 거부되었습니다", Toast.LENGTH_SHORT).show()
            return Result.failure()
        }
        return Result.success()
    }
}