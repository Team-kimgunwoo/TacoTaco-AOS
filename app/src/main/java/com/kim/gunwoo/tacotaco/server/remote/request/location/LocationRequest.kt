package com.kim.gunwoo.tacotaco.server.remote.request.location

import com.google.gson.annotations.SerializedName

data class LocationRequest (
    @field:SerializedName("latitude")
    val latitude: String,
    @field:SerializedName("longitude")
    val longitude: String
)