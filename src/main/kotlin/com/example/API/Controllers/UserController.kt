package com.example.API.Controllers

import com.example.Models.Response.ErrorResponse
import com.example.Services.Implementations.UserService
import com.example.Services.Interfaces.IUserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import java.util.UUID


fun Route.initUserController(){
    val userService by inject<IUserService>()
    route("/user"){
        get("all") {
           var users = userService.getAll()
            if(users.count() == 0){
                call.respond(HttpStatusCode.OK, "Пусто!!!")
                return@get
            }
            call.respond(HttpStatusCode.OK, users)
        }
        get("/{id}") {
            var userId = call.parameters["id"]!!
            var id = UUID.fromString(userId)
            var user = userService.getUserById(id)
            if(user == null){
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(404, "Не найден пользователь!"))
                return@get
            }
            call.respond(HttpStatusCode.OK, user)
        }
    }
}

