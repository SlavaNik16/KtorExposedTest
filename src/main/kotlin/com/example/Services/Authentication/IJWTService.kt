package com.example.Services.Authentication

import com.auth0.jwt.JWTVerifier
import com.example.Models.UserModel

interface IJWTService {

    fun generateToken(user: UserModel):String

    fun getVerifier(): JWTVerifier
}