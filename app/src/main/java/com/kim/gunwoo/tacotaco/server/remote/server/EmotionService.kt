package com.kim.gunwoo.tacotaco.server.remote.server

import com.kim.gunwoo.tacotaco.server.remote.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Query

interface EmotionService {
    @PATCH("/emotion")
    suspend fun patchEmotion(
        @Header("Authorization")accessToken : String = "String",
        @Query("emotionType") emotionType : String
    ): BaseResponse<Unit>
}