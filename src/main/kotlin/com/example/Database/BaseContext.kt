package com.example.Database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

class BaseContext {
    var database = Database.connect(
        url = "jdbc:${System.getenv("DATABASE_URL")}",
        driver = System.getenv("JDBC_DRIVER"),
        user = System.getenv("USER"),
        password = System.getenv("PASSWORD"),
        )
    fun Application.initializeDatabase(){
        //Database.connect()
    }

    fun getDatabase(){

    )

}