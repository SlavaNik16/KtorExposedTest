package com.example.Services.Authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.Models.UserModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class JWTService : IJWTService{
    private val jwtIssuer = "testIssuerJWTToken"
    private val jwtDomain = "https://localhost:5432/"
    private val jwtSecretKey = System.getenv("JWT_SECRET_KEY")
    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecretKey)

    private val verifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withAudience(jwtIssuer)
        .withIssuer(jwtDomain)
        .build()

    override fun generateToken(user:UserModel):String {
        return JWT.create()
            .withSubject("AppAuthentication")
            .withIssuer(jwtIssuer)
            .withClaim("email", user.email)
            .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))
            .sign(jwtAlgorithm)
    }

    override fun getVerifier():JWTVerifier = verifier
}