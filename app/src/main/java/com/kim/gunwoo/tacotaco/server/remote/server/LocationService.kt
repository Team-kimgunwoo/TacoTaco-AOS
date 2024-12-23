package com.kim.gunwoo.tacotaco.server.remote.server

import com.kim.gunwoo.tacotaco.server.remote.request.location.LocationRequest
import com.kim.gunwoo.tacotaco.server.remote.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LocationService {
    @POST("/geo")
    suspend fun postLocation(
        @Header("Authorization")accessToken : String = "String",
        @Body body: LocationRequest
    ): BaseResponse<Unit>
}