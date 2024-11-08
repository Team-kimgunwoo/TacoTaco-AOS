package com.kim.gunwoo.tacotaco.server.remote.request.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("pw")
    val pw: String
)