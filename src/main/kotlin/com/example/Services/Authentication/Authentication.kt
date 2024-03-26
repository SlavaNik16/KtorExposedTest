package com.example.Services.Authentication

import com.example.Services.Authentication.HTTPRequest.Request
import io.ktor.client.request.*
import io.ktor.http.cio.*
import io.ktor.server.request.*
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


private val hashKey = System.getenv("HASH_SECRET_KEY").toByteArray()
private val nameAlgorithm:String ="HmacSHA1"
private val nameAlgorithmAWS:String ="HmacSHA256"
private val hmacKey = SecretKeySpec(hashKey, nameAlgorithm)
fun hashHmacSha1(password:String):String{
    val hmac = Mac.getInstance(nameAlgorithm)
    hmac.init(hmacKey)

    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
fun hashHmacSha256(key:String):String{
    val hmac = Mac.getInstance(nameAlgorithmAWS)
    hmac.init(SecretKeySpec(key.toByteArray(), nameAlgorithmAWS))
    return hex(hmac.doFinal(key.toByteArray(Charsets.UTF_8)))
}
fun hmac(key:String, dop:String):String{
    val hmac = Mac.getInstance(nameAlgorithmAWS)
    hmac.init(SecretKeySpec(key.toByteArray(), nameAlgorithmAWS))
    hmac.update(dop.toByteArray())
    return hex(hmac.doFinal(key.toByteArray(Charsets.UTF_8)))
}



var accessKeyId = "AKIA5TRHZWO3QGECXEHC"
var secretAccessKey=  "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"
var region= "spb"
var serviceName= "s3"
var algorithm = "AWS4-HMAC-SHA256"
var v4Identifier = "aws4_request"

fun canonicalHeaderValues(values:String):String{
    val regex = "\\s+/g".toRegex()
    val regexNext = "/^\\s+|\\s+\$/g".toRegex()
    return values.replace(regex = regex, "").replace(regex = regexNext, "");
}


fun canonicalHeaders(request:Request): String {
    var headersPath = MutableList(0) {mutableListOf<String>()}
    for(i in request.header)
    {
        headersPath.add(mutableListOf(i.key, i.value))
    }
    headersPath.sortBy { it[0].lowercase() }


    var parts = mutableListOf<String>()
    for(i in headersPath)
    {
        var key = i[0].lowercase();
        var value =  i[1]
        parts.add(key + ":" + canonicalHeaderValues(value));
    }


    return parts.joinToString("\n");
}

fun canonicalString(request:Request, postman:String):String {
    var parts = mutableListOf<String>();
    parts.add(request.req["method"].toString());
    parts.add(request.req["pathname"].toString());
    parts.add("");
    parts.add(canonicalHeaders(request).toString()+"\n");
    parts.add(request.req["header"].toString());
    //parts.add(postman);
    return parts.joinToString("\n");
}

fun createScope(date:String, region:String, serviceName:String):String {
    return mutableListOf(
        date.substring(0, 8),
        region,
        serviceName,
        v4Identifier
    ).joinToString("/");
}
fun credentialString(datetime:String):String  {
    return createScope(
        datetime.substring(0, 8),
        region,
        serviceName
    );
}
fun getSigningKey(date:String,region:String,service:String):String
{
    var kDate = hmac("AWS4".plus(secretAccessKey), date)
    var kRegion = hmac(kDate, region);
    var kService = hmac(kRegion, service);
    var signingKey = hmac(kService, v4Identifier);
    return signingKey;
}
fun signature(datetime:String, request:Request, postman:String):String {
    var signingKey = getSigningKey(
        datetime.substring(0, 8),
        region,
        serviceName
    );
    return hmac(signingKey, stringToSign(datetime, request,postman));
}

fun stringToSign(datetime:String, request:Request, postman: String):String {
    var parts = mutableListOf<String>();
    parts.add(algorithm);
    parts.add(datetime);
    parts.add(credentialString(datetime));
    parts.add(hashHmacSha256(canonicalString(request,postman)));
    return parts.joinToString("\n");
}
fun result(request: Request,datetime: String,postman: String):String{
    return authorization(request, datetime, postman)
}
fun resultAll(request: Request,datetime: String,postman: String):String{
        return (canonicalString(request,postman)+"\n"+
            stringToSign(datetime,request,postman)+"\n"+
            signature(datetime,request,postman) + "\n")
}
fun authorization(request:Request, datetime:String,postman:String):String {
    var parts = mutableListOf<String>();
    var credString = credentialString(datetime);
    parts.add("AWS4-HMAC-SHA256 Credential=$accessKeyId/$credString");
    parts.add("SignedHeaders=" + request.req["header"]);
    parts.add("Signature=" + signature(datetime, request, postman));
    return parts.joinToString(", ")
}