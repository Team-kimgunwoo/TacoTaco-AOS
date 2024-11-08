package com.kim.gunwoo.tacotaco.server.remote.responses

import com.google.gson.annotations.SerializedName

data class TokenDataResponses(
    @field:SerializedName("accessToken")
    val accessToken: String,
    @field:SerializedName("refreshToken")
    val refreshToken: String,
)