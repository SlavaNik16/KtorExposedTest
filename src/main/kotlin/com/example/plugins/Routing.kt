package com.example.plugins

import com.example.API.Controllers.initUserController
import com.example.API.Controllers.initValidateS3Controller
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        initUserController()
        initValidateS3Controller()
    }
}