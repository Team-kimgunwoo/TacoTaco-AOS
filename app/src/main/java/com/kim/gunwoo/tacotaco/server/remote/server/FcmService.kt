package com.kim.gunwoo.tacotaco.server.remote.server

import com.kim.gunwoo.tacotaco.server.remote.responses.BaseResponse
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmService {
    @POST("/fcm/token")
    suspend fun postFcm(
        @Header("Authorization")accessToken : String = "String",
        @Query("fcmToken") fcmToken : String
    ): BaseResponse<Unit>
}