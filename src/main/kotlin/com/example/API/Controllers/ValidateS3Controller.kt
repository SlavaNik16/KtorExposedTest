package com.example.API.Controllers

import com.example.Models.Response.ErrorResponse
import com.example.Services.Authentication.AWS.AWSV4Auth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.TreeMap

fun Route.initValidateS3Controller() {
    route("/s3") {
        get("/validate") {
            try {
                var postman = call.request.headers["x-amz-content-sha256"]
                var headers = TreeMap<String,String>()
                headers.put("host", "127.0.0.1:8080")
                headers.put("x-amz-content-sha256", postman.toString())
                var resultMap = AWSV4Auth.Builder("AKIA5TRHZWO3QGECXEHC","jvIMMNOMEI7MoskR2rblLUZlInHE4FAYSZiyeiKK")
                    .awsHeaders(headers)
                    .debug()
                    .build()
                    .getHeaders()
                if(resultMap == null){
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос") )
                    return@get
                }
                var result = resultMap["Authorization"]
                if(result == null){
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос") )
                    return@get
                }
                call.respond(HttpStatusCode.OK,result )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(406, e.message ?: Constants.GENERAL))
            }
        }
    }
}