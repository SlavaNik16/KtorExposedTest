package com.example.Context.Database


import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.UserTable
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DatabaseFactory
{
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = System.getenv("DATABASE_URL")
    private val dbUser = System.getenv("DATABASE_USER")
    private val dbPassword =System.getenv("DATABASE_PASSWORD")

    fun Application.initializeDatabase(){
        Database.connect(getHikariDataSource())

        transaction {
            SchemaUtils.create(
                UserTable,
                CardTable
            )
        }
    }

    fun getHikariDataSource():HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:$dbUrl"
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T {
        return withContext(Dispatchers.IO){
            transaction {
                block()
            }
        }
    }




}