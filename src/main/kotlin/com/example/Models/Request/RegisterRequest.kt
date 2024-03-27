package com.example.Models.Request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    var surname: String,
    var name: String,
    var email: String,
    var password: String,
)