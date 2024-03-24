package com.example.plugins

import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import com.example.Models.UserModel
import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Services.Authentication.JWTService
import com.example.Services.Interfaces.IUserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.configureSecurity() {
    val jwtService = JWTService()
    val userService by inject<IUserService>()
    val jwtRealm = "ktor sample app test"
    runBlocking {
        userService.create(
            UserModel(
                 id=  UUID.randomUUID(),
                surname = "Николаев",
                name = "Вячеслав",
                email = "nik@gmail2.com",
                password = "Test12345",
                roleType = RoleTypes.Admin,
                statusType = StatusTypes.Online,
            )
        )
    }
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
