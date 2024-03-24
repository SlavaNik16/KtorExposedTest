package com.example

import com.example.Context.Database.DatabaseFactory.initializeDatabase
import com.example.plugins.configureMonitoring
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    initializeDatabase()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
//    configureDatabases()
//    configureRouting()
}
