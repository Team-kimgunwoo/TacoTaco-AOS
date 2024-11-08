package com.kim.gunwoo.tacotaco.server.remote.interceptor

import android.util.Log
import com.kim.gunwoo.tacotaco.server.local.TacotacoDB
import com.kim.gunwoo.tacotaco.server.local.TokenEntity
import com.kim.gunwoo.tacotaco.server.remote.RetrofitBuilder
import com.kim.gunwoo.tacotaco.server.remote.request.login.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class RefreshInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val tokenDao = TacotacoDB.getInstanceOrNull() ?: throw RuntimeException()
        val urlPath = response.request.url.toString().substring(35)

        Log.d("RefreshInterceptor", "result : ${response.code == 401 && !(urlPath == "/auth/login" || urlPath == "/auth/email-verify" || urlPath == "/auth/email-send" || urlPath == "/auth/join")}")
        Log.d("RefreshInterceptor", "urlPath : $urlPath")
        Log.d("RefreshInterceptor", "code : ${response.code == 401 }")
        Log.d("RefreshInterceptor", "/auth/join : ${ urlPath == "/auth/join"}")

        if ((tokenDao.tokenDao().getMembers().accessToken.isNotBlank()) && response.code == 401 && !(urlPath == "/auth/login")) {
            Log.d("RefreshInterceptor", "in funfun true")

            // runBlocking을 사용하여 비동기 코드를 동기적으로 처리
            val newAccessToken = runBlocking {
                var token: String = ""
                val result = kotlin.runCatching {
                    RetrofitBuilder.getLoginService().requestRefresh(
                        RefreshRequest(
                            refreshToken = tokenDao.tokenDao().getMembers().refreshToken
                        )
                    )
                }

                result.onSuccess { response ->
                    if (response.data != null) {
                        token = response.data
                    }
                }.onFailure {
                    it.printStackTrace()
                }
                token // 최종적으로 반환할 토큰
            }

            if (newAccessToken.isNotEmpty()) {
                // 토큰을 저장
                tokenDao.tokenDao().updateMember(
                    TokenEntity(
                        id = tokenDao.tokenDao().getMembers().id,
                        accessToken = newAccessToken,
                        refreshToken = tokenDao.tokenDao().getMembers().refreshToken,
                    )
                )

                response.close()
                // 새로운 요청 생성
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
                return chain.proceed(newRequest) // 새로운 요청으로 대체
            }
        }

        // 기존 응답 반환
        return response
    }
}