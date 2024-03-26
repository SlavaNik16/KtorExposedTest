package com.example.API.Controllers

import com.example.Context.Database.Tables.Models.UserTable.email
import com.example.Models.Request.LoginRequest
import com.example.Models.Request.RegisterRequest
import com.example.Models.Response.ErrorResponse
import com.example.Models.Response.TokenResponse
import com.example.Models.UserModel
import com.example.Services.Authentication.hashHmacSha1
import com.example.Services.Interfaces.IUserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.UUID


fun Route.initUserController() {

    var hashFunction = { p: String -> hashHmacSha1(p) }
    val userService by inject<IUserService>()
    route("/user") {
        get("all") {
            try {
                var users = userService.getAll()
                if (users.count() == 0) {
                    call.respond(HttpStatusCode.OK, Constants.IS_EMPTY)
                    return@get
                }
                call.respond(HttpStatusCode.OK, users)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }
        get("/id/{id}") {
            try {
                var userId = call.parameters["id"]!!
                var id = UUID.fromString(userId)
                var user = userService.getUserById(id)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse(404, Constants.NOT_FOUND_USER))
                    return@get
                }
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }

        get("/email/{email}") {
            try {
                var email = call.parameters["email"]!!
                var user = userService.getUserByEmail(email)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse(404, Constants.NOT_FOUND_USER))
                    return@get
                }
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }

        post("/reg") {
            var reqisterRequest = call.receiveNullable<RegisterRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(400, Constants.GENERAL))
                return@post
            }
            try {
                var userModel = UserModel(
                    surname = reqisterRequest.surname.trim(),
                    name = reqisterRequest.name.trim(),
                    email = reqisterRequest.email.trim(),
                    password = hashFunction(reqisterRequest.password)
                )
                userService.create(userModel)
                call.respond(HttpStatusCode.OK, TokenResponse(userService.generateAccessToken(userModel)))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }

        post("/auth") {
            var loginRequest = call.receiveNullable<LoginRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(400, Constants.GENERAL))
                return@post
            }
            try {
                var email = loginRequest.email.trim()
                var password = loginRequest.password
                val user = userService.getUserByEmail(email)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse(404, Constants.NOT_FOUND_USER))
                    return@post
                }
                if (user.password != hashFunction(password)) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse(404, Constants.NOT_FOUND_USER))
                    return@post
                }
                call.respond(HttpStatusCode.OK, TokenResponse(userService.generateAccessToken(user)))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }
    }
}

