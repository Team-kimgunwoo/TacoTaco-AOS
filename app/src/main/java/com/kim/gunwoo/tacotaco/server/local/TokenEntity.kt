package com.kim.gunwoo.tacotaco.server.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_table")
data class TokenEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "accessToken")
    val accessToken: String,

    @ColumnInfo(name = "refreshToken")
    val refreshToken: String
)