package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.Repositories.Implementation.UserRepository
import com.example.Services.Authentication.JWTService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureSecurity() {
    val jwtService = JWTService()
    var repository = UserRepository()
//    authentication {
//        jwt {
//            realm = jwtRealm
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256(jwtSecretKey))
//                    .withAudience(jwtAudience)
//                    .withIssuer(jwtDomain)
//                    .build()
//            )
//            validate {
//                credential ->
//                if(credential.payload.audience.contains(jwtAudience)) {
//                    JWTPrincipal(credential.payload)
//                } else null
//            }
//        }
//    }
}
