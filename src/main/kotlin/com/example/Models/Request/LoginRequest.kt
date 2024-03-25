package com.example.Models.Request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    var email: String,
    var password: String,
)