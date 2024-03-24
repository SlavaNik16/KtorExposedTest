package com.example.plugins

import com.example.Registrations.cardModule
import com.example.Registrations.userModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(userModule)
        modules(cardModule)
    }
}
