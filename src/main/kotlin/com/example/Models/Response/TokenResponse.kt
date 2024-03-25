package com.example.Models.Response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse (
    val accessToken: String
)