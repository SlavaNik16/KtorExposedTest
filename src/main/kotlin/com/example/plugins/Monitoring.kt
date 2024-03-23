package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
       level = Level.INFO
        filter { call->
            call.request.path().startsWith("/")
        }
    }
}
