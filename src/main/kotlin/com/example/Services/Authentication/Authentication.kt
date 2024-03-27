package com.example.Services.Authentication

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


private val hashKey = System.getenv("HASH_SECRET_KEY").toByteArray()
private val nameAlgorithm: String = "HmacSHA1"
private val hmacKey = SecretKeySpec(hashKey, nameAlgorithm)
fun hashHmacSha1(password: String): String {
    val hmac = Mac.getInstance(nameAlgorithm)
    hmac.init(hmacKey)

    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}