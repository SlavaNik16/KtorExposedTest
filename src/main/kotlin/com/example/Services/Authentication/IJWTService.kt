package com.example.Services.Authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.example.Models.UserModel
import java.time.LocalDateTime
import java.time.ZoneOffset

interface IJWTService {

    fun generateToken(user: UserModel):String

    fun getVerifier(): JWTVerifier
}