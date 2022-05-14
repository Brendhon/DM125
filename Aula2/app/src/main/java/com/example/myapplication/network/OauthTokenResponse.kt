package com.example.myapplication.network

/* Library to serialize */
import com.squareup.moshi.Json

data class OauthTokenResponse(
    @Json(name = "access_token") // Name in the JSON
    val accessToken: String,

    @Json(name = "expires_in") // Name in the JSON
    val expiresIn: Int,
)
