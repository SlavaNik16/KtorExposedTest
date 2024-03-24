package com.example.plugins

import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Services.Authentication.JWTService
import io.ktor.server.application.*

fun Application.configureSecurity() {
    val jwtService = JWTService()
    var repository = UserReadRepository()
    val jwtRealm = "ktor sample app test"
//    authentication {
//        jwt {
//            realm = jwtRealm
//            verifier(jwtService.getVerifier())
//            validate {
//                credential ->
//                val payload = credential.payload
//                val email = payload.getClaim("email").asString()
//                val user =
//            }
//        }
//    }
}
