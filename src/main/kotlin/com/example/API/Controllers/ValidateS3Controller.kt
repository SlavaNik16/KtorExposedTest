package com.example.API.Controllers

import ch.qos.logback.core.subst.Token
import com.example.Models.Response.ErrorResponse
import com.example.Models.Response.TokenResponse
import com.example.Services.Authentication.DateHelper.toIsoString
import com.example.Services.Authentication.HTTPRequest.Request
import com.example.Services.Authentication.hashHmacSha256
import com.example.Services.Authentication.result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.javatime.Date
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

fun Route.initValidateS3Controller() {
    route("/s3") {
        get("/validate") {
            try {
                val text = call.receiveText()
                val stringDate = Date().toIsoString()
                var request = Request(
                    header = mutableMapOf(
                        "host" to "s3/validate",
                        "x-amz-content-sha256" to "UNSIGNED-PAYLOAD",
                        "x-amz-date" to stringDate+"Z"
                    ),
                    req = mutableMapOf(
                        "header" to "",
                        "pathname" to "/$text",
                        "method" to "GET",
                        "bodyhash" to "UNSIGNED-PAYLOAD"
                    )
                )
                request.req["header"] = request.header.keys.joinToString(";")
                var result = result(request,stringDate)\
                if(result == null){
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос") )
                }
                call.respond(HttpStatusCode.OK, result == )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }
    }
}