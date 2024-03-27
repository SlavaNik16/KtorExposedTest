package com.example.API.Controllers

import aws.sdk.kotlin.services.s3.express.sigV4S3Express
import aws.smithy.kotlin.runtime.InternalApi
import aws.smithy.kotlin.runtime.text.encoding.decodeBase64
import com.example.Models.Response.ErrorResponse
import com.example.Services.Authentication.AWS.AWSV4Auth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.Identity.encode
import org.apache.commons.codec.binary.Base64
import java.nio.channels.ByteChannel
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.TreeMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@OptIn(InternalApi::class)
fun Route.initValidateS3Controller() {
    get("") {
            try {
//                val dynamoDbClient = DynamoDbClient {
//                    region = "us-east-1"
//                    credentialsProvider = ProfileCredentialsProvider(profileName = "myprofile")
//                }

               //var message =call.parameters["message"]
                var postman = call.request.headers["x-amz-content-sha256"]
                var headers = TreeMap<String,String>()
                headers.put("host", "127.0.0.1:8080")
                headers.put("x-amz-content-sha256", postman.toString())
//                headers.put("Content-Type", "")
//                headers.put("Content-MD5", "")
                //var queryParametes = TreeMap<String,String>()
                //queryParametes.put("message", message.toString())
                var access_key = "AKIAIOSFODNN7EXAMPLE"
                var secret_key = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
                var resultMap =
                    AWSV4Auth.Builder(access_key,secret_key)
                        .canonicalURI(null)
                    //.queryParametes(queryParametes)
                    .awsHeaders(headers)
                    .payload(null)
                    .debug()
                    .build()
                    .getHeaders()
                if(resultMap == null){
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос") )
                    return@get
                }
                var result = resultMap["Authorization"]
                var resultServer = call.request.headers["Authorization"]
                if(result == null || resultServer == null){
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse(403, "Недействительный запрос") )
                    return@get
                }
                call.respond(HttpStatusCode.OK,result + "\n" + resultServer)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(409, e.message ?: Constants.GENERAL))
            }
    }
}