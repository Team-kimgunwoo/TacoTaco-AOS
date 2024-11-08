package com.kim.gunwoo.tacotaco.server.remote.server

import com.kim.gunwoo.tacotaco.server.remote.request.login.LoginRequest
import com.kim.gunwoo.tacotaco.server.remote.request.login.RefreshRequest
import com.kim.gunwoo.tacotaco.server.remote.responses.BaseResponse
import com.kim.gunwoo.tacotaco.server.remote.responses.TokenDataResponses
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/auth/login")
    suspend fun postLogin(
        @Body body: LoginRequest
    ): BaseResponse<TokenDataResponses>

    @POST("/auth/reissue")
    suspend fun requestRefresh(
        @Body refreshToken : RefreshRequest
    ) : BaseResponse<String>

}