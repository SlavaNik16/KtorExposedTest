package com.example.Models.Response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String,
)