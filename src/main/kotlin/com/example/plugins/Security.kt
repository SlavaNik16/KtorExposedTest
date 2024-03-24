package com.example.plugins

import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Services.Authentication.JWTService
import com.example.Services.Interfaces.IUserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val jwtService = JWTService()
    val userService by inject<IUserService>()
    val jwtRealm = "ktor sample app test"
    authentication {
        jwt {
            realm = jwtRealm
            verifier(jwtService.getVerifier())
            validate {
                credential ->
                val payload = credential.payload
                val email = payload.getClaim("email").asString()
                val user = userService.getUserByEmail(email)
                user
            }
        }
    }
}
