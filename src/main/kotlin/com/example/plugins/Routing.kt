package com.example.plugins

import com.example.API.Controllers.initUserController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){
    routing {
        initUserController()
    }
}