package com.example.API.Controllers

import com.example.Models.Response.ErrorResponse
import com.example.Services.Authentication.AWS.AWSV4Auth
import com.example.Services.Authentication.AWS.AwsBuilder
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.initValidateS3Controller() {
    route("/aws") {
        get("verification") {
            try {
                var postman = call.request.headers["x-amz-content-sha256"]
                var headers = TreeMap<String, String>()
                headers.put("host", "127.0.0.1:8080")
                headers.put("x-amz-content-sha256", postman.toString())
                var access_key = "AKIAIOSFODNN7EXAMPLE"
                var secret_key = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"

                val awsBuilder = AwsBuilder(access_key, secret_key)
                var resultMap =
                    AWSV4Auth(awsBuilder).getValidateStart()
                if (resultMap == null) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос"))
                    return@get
                }
                var result = resultMap["Authorization"]
                var resultServer = call.request.headers["Authorization"]
                if (result == null || resultServer == null) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос"))
                    return@get
                }
                call.respond(HttpStatusCode.OK, result + "\n" + resultServer)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(409, e.message ?: Constants.GENERAL))
            }
        }
    }
}