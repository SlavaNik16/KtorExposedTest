package com.example.API.Controllers

import com.example.Services.Implementations.UserService
import com.example.Services.Interfaces.IUserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject


fun Route.initUserController(){
    val userService by inject<IUserService>()
    route("/user"){
        get("all") {
           var users = userService.getAll()
            call.respond(HttpStatusCode.OK, users)
        }
    }
}

