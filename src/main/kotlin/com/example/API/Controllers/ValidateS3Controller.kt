package com.example.API.Controllers

import com.example.Models.Request.RegisterRequest
import com.example.Models.Response.ErrorResponse
import com.example.Models.UserModel
import com.example.Services.Authentication.AWS.AWSV4Auth
import com.example.Services.Authentication.AWS.AwsBuilder
import com.example.Services.Authentication.hashHmacSha1
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

fun Route.initValidateS3Controller() {
    fun getValidate(access_key: String, secret_key:String, request:ApplicationRequest):AwsBuilder {
        var awsHeader = TreeMap<String, String>()
        awsHeader.put("host", "127.0.0.1:8080")

        return AwsBuilder(access_key, secret_key)
            .initHTTPMethod(request.httpMethod)
            .initCanonicalURI(request.path())
            .initQueryParametes(awsHeader)
            .initAwsHeaders(awsHeader)
            .initPayload(null)
            .initDebug()
    }
    var access_key = "AKIAIOSFODNN7EXAMPLE"
    var secret_key = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    route("/aws") {
        get("/verification") {
            try {
                var genereteToken =getValidate(access_key, secret_key, call.request)
                    .build()
                    .getValidateStart()
                var tokenAnswer = call.request.headers["Authorization"]
                if (genereteToken != tokenAnswer) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос"))
                    return@get
                }
                call.respond(HttpStatusCode.OK, "Токен действительный!")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(409, e.message ?: Constants.GENERAL))
            }
        }

        post("/create") {
            try {
                var message = call.parameters["message"]
                var list_type = call.parameters["list-type"]
                var queryParametes = TreeMap<String, String>()
                queryParametes.put("message", message.toString())
                queryParametes.put("list-type", list_type.toString())

                var genereteToken =getValidate(access_key, secret_key, call.request)
                    .initQueryParametes(queryParametes)
                    .build()
                    .getValidateStart()
                var tokenAnswer = call.request.headers["Authorization"]
                if (genereteToken != tokenAnswer) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос"))
                    return@post
                }
                call.respond(HttpStatusCode.OK, "Токен действительный! Были отправлены следующие данные:\n$queryParametes")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(409, e.message ?: Constants.GENERAL))
            }
        }
    }
}