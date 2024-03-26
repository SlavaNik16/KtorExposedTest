package com.example.Services.Authentication.AWS

import io.ktor.util.*
import io.ktor.utils.io.core.*
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.Crypt
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.HmacUtils
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Digest
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.HMac
import java.math.BigInteger
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toByteArray


class AWSV4Auth {

    constructor(builder: Builder) {
        accessKeyID = builder.accessKeyID;
        secretAccessKey = builder.secretAccessKey;
        regionName = builder.regionName;
        serviceName = builder.serviceName;
        httpMethodName = builder.httpMethodName;
        canonicalURI = builder.canonicalURI;
        queryParametes = builder.queryParametes;
        awsHeaders = builder.awsHeaders;
        payload = builder.payload;
        debug = builder.debug;
    }
    class Builder {

        var accessKeyID: String
        var secretAccessKey: String
        var regionName: String =  "eu-west-1"
        var serviceName: String = "s3"
        var httpMethodName: String = "GET"
        var canonicalURI: String? = null
        var queryParametes: TreeMap<String, String> = TreeMap()
        var awsHeaders: TreeMap<String, String> = TreeMap()
        var payload: String? = null
        var debug = false

        constructor(accessKeyID:String, secretAccessKey:String)
        {
            this.accessKeyID = accessKeyID;
            this.secretAccessKey = secretAccessKey;
        }

        fun regionName(regionName:String): Builder
        {
            this.regionName = regionName;
            return this;
        }


        fun serviceName(serviceName:String): Builder
        {
            this.serviceName = serviceName;
            return this;
        }

        fun httpMethodName(httpMethodName:String): Builder
        {
            this.httpMethodName = httpMethodName;
            return this;
        }

        fun canonicalURI(canonicalURI:String?): Builder
        {
            this.canonicalURI = canonicalURI;
            return this;
        }

        fun queryParametes(queryParametes:TreeMap<String, String> ): Builder
        {
            this.queryParametes = queryParametes;
            return this;
        }

        fun awsHeaders(awsHeaders:TreeMap<String, String>): Builder
        {
            this.awsHeaders = awsHeaders;
            return this;
        }

        fun payload(payload:String?): Builder
        {
            this.payload = payload;
            return this;
        }

        fun debug(): Builder
        {
            this.debug = true;
            return this;
        }

        fun build(): AWSV4Auth
        {
            return AWSV4Auth(this);
        }
    }

    private var accessKeyID: String
    private var secretAccessKey: String
    private var regionName: String =  "eu-west-1"
    private var serviceName: String = "s3"
    private var httpMethodName: String = "GET"
    private var canonicalURI: String? = null
    private var queryParametes: TreeMap<String, String>
    private var awsHeaders: TreeMap<String, String>
    private var payload: String? = null
    private var debug = false
    private var strSignedHeader: String = "/ser/validate"
    private var xAmzDate: String = getTimeStamp()
    private var currentDate: String = getDate()


    /**
     * Создайние канонического запроса
     */
    private fun prepareCanonicalRequest(): String {
        val canonicalURL = StringBuilder("")
        canonicalURL.append(httpMethodName).append("\n")
        canonicalURI = if (canonicalURI == null || canonicalURI!!.trim().isEmpty()) "/" else URLEncoder.encode(canonicalURI)
        canonicalURL.append(canonicalURI).append("\n")
        val queryString = StringBuilder("")
        if (queryParametes != null && !queryParametes.isEmpty()) {
            for ((key, value) in queryParametes.entries) {
                queryString.append(URLEncoder.encode(key)).append("=").append(encodeParameter(URLEncoder.encode(value))).append("&")
            }
            queryString.deleteCharAt(queryString.lastIndexOf("&"))
        }
        queryString.append("\n")
        canonicalURL.append(queryString)

        val signedHeaders = StringBuilder("")
        if (awsHeaders != null && !awsHeaders.isEmpty()) {
            for ((key, value) in awsHeaders.entries) {
                signedHeaders.append(key.lowercase()).append(";")
                canonicalURL.append(key.lowercase()).append(":").append(value.trim()).append("\n")
            }
        }else{
            canonicalURL.append("\n")
        }
        strSignedHeader = signedHeaders.substring(0, signedHeaders.length - 1)
        canonicalURL.append(strSignedHeader).append("\n")
        payload = if (payload == null) "" else payload
        canonicalURL.append((generateHex(payload!!)))
        if (debug) {
            println("##Canonical Request:\n$canonicalURL\n")
        }

        return canonicalURL.toString()
    }

    /**
     * Создание строки для подписи
     */
    private fun prepareStringToSign(canonicalURL: String): String {
        var stringToSign = (HMACAlgorithm + "\n" +
                                xAmzDate + "\n" +
                                (currentDate + "/" + regionName + "/" + serviceName + "/" + aws4Request) + "\n" +
                                generateHex(canonicalURL))
        if (debug) {
            println("##String to sign:\n$stringToSign")
        }

        return stringToSign
    }

    private fun calculateSignature(stringToSign: String): String? {
        try {
            val signatureKey = getSignatureKey(secretAccessKey, currentDate, regionName, serviceName)
            val signatureKey2 = getSignatureKey2(secretAccessKey, currentDate, regionName, serviceName)
            val signature = HmacSHA256(signatureKey, stringToSign)
            val signature2 = HmacSHA256(signatureKey2, stringToSign)
            val strHexSignature = bytesToHex(signature)
            println("Примеры!: ")
            println(bytesToHex(signature2))
            println(Hex.encodeHex(DigestUtils.sha256(signature)))
            println(DigestUtils.sha256Hex(signature))
            println(HmacUtils.hmacSha256(signatureKey, stringToSign.toByteArray()))
            println()
            return strHexSignature
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun getHeaders(): Map<String, String>? {
        awsHeaders.put("x-amz-date", xAmzDate)
        val canonicalURL = prepareCanonicalRequest()
        val stringToSign = prepareStringToSign(canonicalURL)
        val signature = calculateSignature(stringToSign)

        if (signature != null) {
            val header: MutableMap<String, String> = HashMap(0)
            header["x-amz-date"] = xAmzDate.toString()
            header["Authorization"] = buildAuthorizationString(signature)

            if (debug) {
                println("##Signature:\n$signature")
                println("##Header:")
                for ((key, value) in header) {
                    println("$key = $value")
                }
                println("================================")
            }
            return header
        } else {
            if (debug) {
                println("##Signature:\n$signature")
            }
            return null
        }
    }

    private fun buildAuthorizationString(strSignature: String): String {
        return (HMACAlgorithm + " "
                + "Credential=" + accessKeyID + "/" + getDate() + "/" + regionName + "/" + serviceName + "/" + aws4Request + ", "
                + "SignedHeaders=" + strSignedHeader + ", "
                + "Signature=" + strSignature)
    }


    private  val hexArray = "0123456789ABCDEF".lowercase().toByteArray(StandardCharsets.US_ASCII)

    //HEX()
    private fun bytesToHex(bytes:ByteArray): String  {
        var hexChars = ByteArray(bytes.size * 2);
        for (j in bytes.indices) {
            var v = bytes[j].toInt() and 0xFF;
            hexChars[j * 2] = hexArray[v.ushr(4)];
            hexChars[j * 2 + 1] = hexArray[v and 0x0F];
        }
        return String(hexChars, StandardCharsets.UTF_8)
    }


    /**
     * Sha256
     */
//    fun generateHex(data:String):String{ //Sha256
//        var digest  = MessageDigest.getInstance("SHA-256")
//        var mesageDijest =digest.digest(data.toByteArray())
//        return String.format("%064x", BigInteger(1, mesageDijest))
//    }
    fun generateHex(data:String):String{
        var digest  = MessageDigest.getInstance("SHA-256")
        digest.update(data.toByteArray(Charsets.UTF_8))
        var digests: ByteArray = digest.digest()
        return String.format("%064x", BigInteger(1, digests))
    }


    fun HmacSHA256(key:ByteArray, data:String):ByteArray{
        val algorithm = "HmacSHA256"
        val hmac = Mac.getInstance(algorithm)
        hmac.init(SecretKeySpec(key, algorithm))
        return hmac.doFinal(data.toByteArray(Charsets.UTF_8))
    }

    /**
     * HMAC-SHA256
     */
//    fun HmacSHA256(key:String , data:String):String {
//        val sha256_HMAC = Mac.getInstance("HmacSHA256")
//        val secret_key = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA256")
//        sha256_HMAC.init(secret_key)
//         return hex(sha256_HMAC.doFinal(data.toByteArray(Charsets.UTF_8)))
//}


    fun getSignatureKey(key:String,  date:String, regionName:String, serviceName:String):ByteArray
    {
        var KSecret = ("AWS4"+key).toByteArray()
        var kDate = HmacSHA256(KSecret,date)
        var kRegion = HmacSHA256(kDate, regionName);
        var kService = HmacSHA256(kRegion, serviceName);
        var signingKey = HmacSHA256(kService, aws4Request);
        return signingKey
    }
    fun getSignatureKey2(key:String,  date:String, regionName:String, serviceName:String):ByteArray
    {
        var KSecret = ("AWS4"+key)
        var kDate = HmacUtils.hmacSha256(KSecret,date)
        var kRegion = HmacUtils.hmacSha256(kDate.toString(), regionName);
        var kService = HmacUtils.hmacSha256(kRegion.toString(), serviceName);
        var signingKey = HmacUtils.hmacSha256(kService.toString(), aws4Request);
        return signingKey
    }

    private fun getTimeStamp():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        return dateFormat.format(Date())
        //return "Tue, 26 Mar 2024 18:32:04 GMT"
    }

    private fun getDate():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMМdd")
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        return dateFormat.format(Date())
        //return "20240326"
    }

    private fun encodeParameter(param:String):String{
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (e:Exception) {
            return URLEncoder.encode(param);
        }
    }

    companion object {
        private const val HMACAlgorithm = "AWS4-HMAC-SHA256"
        private const val aws4Request = "aws4_request"
    }

}