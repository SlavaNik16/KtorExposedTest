package com.example.Models.Request

data class RegisterRequest (
    var surname: String,
    var name: String,
    var email: String,
    var password: String,
)