package com.kim.gunwoo.tacotaco.server.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kim.gunwoo.tacotaco.server.local.TacotacoDB
import com.kim.gunwoo.tacotaco.server.local.TokenDAO
import com.kim.gunwoo.tacotaco.server.remote.interceptor.RefreshInterceptor
import com.kim.gunwoo.tacotaco.server.remote.server.LoginService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class RetrofitBuilder {
    companion object {
        private var gson: Gson? = null
        private var retrofit: Retrofit? = null
        private var tokenDao: TokenDAO? = null
        private var loginService: LoginService? = null

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
                val interceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
                retrofit = Retrofit.Builder()
                    .baseUrl(Url.serverUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(getGson()!!))
                    .build()
            }
            return retrofit!!
        }

        @Synchronized
        fun getHttpTokenRetrofit(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Url.serverUrl)
                    .client(getTokenOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(getGson()!!))
                    .build()
            }
            return retrofit!!
        }

        @Synchronized
        fun getHttpRetrofit(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Url.serverUrl)
                    .client(getOhHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(getGson()!!))
                    .build()
            }
            return retrofit!!
        }


        @Synchronized
        fun getOhHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val refreshInterceptor = RefreshInterceptor()
            tokenDao = TacotacoDB.getInstanceOrNull()?.tokenDao()
            val httpClient = OkHttpClient.Builder().apply {

            }
            return httpClient
                .addInterceptor(refreshInterceptor)
                .addInterceptor(interceptor)
                .build()
        }

        @Synchronized // 로그인 일회용
        fun getTokenOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val okhttpBuilder = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
//                .addInterceptor(TokenInterceptor())
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            okhttpBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            okhttpBuilder.hostnameVerifier { hostname, session -> true }
            return okhttpBuilder.build()
        }

        fun getLoginService(): LoginService {
            if (loginService == null) {
                loginService = getRetrofit().create(LoginService::class.java)
            }
            return loginService!!
        }

    }
}