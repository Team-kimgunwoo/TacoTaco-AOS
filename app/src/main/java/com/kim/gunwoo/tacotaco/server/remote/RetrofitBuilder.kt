package com.kim.gunwoo.tacotaco.server.remote

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kim.gunwoo.tacotaco.server.remote.request.location.LocationRequest
import com.kim.gunwoo.tacotaco.server.remote.server.EmotionService
import com.kim.gunwoo.tacotaco.server.remote.server.FcmService
import com.kim.gunwoo.tacotaco.server.remote.server.LocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder {
    companion object {
        private var gson: Gson? = null
        private var retrofit: Retrofit? = null
        private var locationService: LocationService? = null
        private var emotionService: EmotionService? = null
        private var fcmService: FcmService? = null

        @Synchronized
        fun getGson(): Gson? {
            if (gson == null) {
                gson = GsonBuilder().setLenient().create()
            }

            return gson
        }


        @Synchronized
        fun getRetrofit(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Url.serverUrl)
                    .addConverterFactory(GsonConverterFactory.create(getGson()!!))
                    .build()
            }
            return retrofit!!
        }



        fun sendLocationToServer(latitude: Double, longitude: Double) {
            Log.d("위치", "sendLocationToServer: ${latitude}")
            CoroutineScope(Dispatchers.IO).launch{
               getLocationService().postLocation(
                    accessToken = "Bearer ${Url.assess}",
                    LocationRequest(latitude = "$latitude", longitude="$longitude"))
            }
        }

        fun patchEmotionService(): EmotionService{
            if (emotionService == null) {
                emotionService = getRetrofit().create(EmotionService::class.java)
            }
            return emotionService!!
        }


        fun postFcmService(): FcmService{
            if (fcmService == null) {
                fcmService = getRetrofit().create(FcmService::class.java)
            }
            return fcmService!!
        }


        fun getLocationService(): LocationService {
            if (locationService == null) {
                locationService = getRetrofit().create(LocationService::class.java)
            }
            return locationService!!
        }

    }
}