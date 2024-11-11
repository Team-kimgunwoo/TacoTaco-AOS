package com.kim.gunwoo.tacotaco.server.remote.server

import com.kim.gunwoo.tacotaco.server.remote.request.location.LocationRequest
import com.kim.gunwoo.tacotaco.server.remote.request.login.LoginRequest
import com.kim.gunwoo.tacotaco.server.remote.responses.BaseResponse
import com.kim.gunwoo.tacotaco.server.remote.responses.TokenDataResponses
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationService {
    @POST("/geo")
    suspend fun postLocation(
        @Body body: LocationRequest
    ): BaseResponse<Unit>
}