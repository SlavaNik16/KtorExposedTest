package com.example.Services.Authentication.AWS

import io.ktor.util.*
import java.math.BigInteger
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

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
        var regionName: String =  "spb"
        var serviceName: String = "s3"
        var httpMethodName: String = "GET"
        var canonicalURI: String = "/"
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

        fun canonicalURI(canonicalURI:String): Builder
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
    private var regionName: String =  "spb"
    private var serviceName: String = "s3"
    private var httpMethodName: String = "GET"
    private var canonicalURI: String = "/"
    private var queryParametes: TreeMap<String, String>
    private var awsHeaders: TreeMap<String, String>
    private var payload: String? = null
    private var debug = false
    private var strSignedHeader: String = "/ser/validate"
    private var xAmzDate: String = getTimeStamp()
    private var currentDate: String = getDate()
    private fun prepareCanonicalRequest(): String {
        val canonicalURL = StringBuilder("")
        canonicalURL.append(httpMethodName).append("\n")
        canonicalURI = if (canonicalURI == null || canonicalURI!!.trim().isEmpty()) "/" else canonicalURI
        canonicalURL.append(canonicalURI).append("\n")
        val queryString = StringBuilder("")
        if (queryParametes != null && !queryParametes.isEmpty()) {
            for ((key, value) in queryParametes.entries) {
                queryString.append(key).append("=").append(encodeParameter(value)).append("&")
            }
            queryString.deleteCharAt(queryString.lastIndexOf("&"))
            queryString.append("\n")
        } else {
            queryString.append("\n")
        }
        canonicalURL.append(queryString)

        val signedHeaders = StringBuilder("")
        if (awsHeaders != null && !awsHeaders.isEmpty()) {
            for ((key, value) in awsHeaders.entries) {
                signedHeaders.append(key).append(";")
                canonicalURL.append(key).append(":").append(value).append("\n")
            }
            canonicalURL.append("\n")
        } else {
            canonicalURL.append("\n")
        }

        strSignedHeader = signedHeaders.substring(0, signedHeaders.length - 1)
        canonicalURL.append(strSignedHeader).append("\n")
        payload = if (payload == null) "" else payload
        canonicalURL.append(generateHex(payload.toString()))

        if (debug) {
            println("##Canonical Request:\n$canonicalURL")
        }

        return canonicalURL.toString()
    }

    private fun prepareStringToSign(canonicalURL: String): String {
        var stringToSign = HMACAlgorithm + "\n"
        stringToSign += xAmzDate + "\n"
        stringToSign += ((currentDate + "/" + regionName).toString() + "/" + serviceName + "/" + aws4Request).toString() + "\n"
        stringToSign += generateHex(canonicalURL)

        if (debug) {
            println("##String to sign:\n$stringToSign")
        }

        return stringToSign
    }

    private fun calculateSignature(stringToSign: String): String? {
        try {
            val signatureKey: ByteArray = getSignatureKey(secretAccessKey, currentDate, regionName, serviceName)
            val signature = HmacSHA256(signatureKey, stringToSign)
            val strHexSignature = bytesToHex(signature)
            return strHexSignature
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun getHeaders(): Map<String, String>? {
        awsHeaders?.put("x-amz-date", xAmzDate.toString())
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


    fun getSignatureKey(key:String,  date:String, regionName:String, serviceName:String):ByteArray
    {
        var KSecret = ("AWS4"+key).toByteArray(Charsets.UTF_8)
        var kDate = HmacSHA256(KSecret,date)
        var kRegion = HmacSHA256(kDate, regionName);
        var kService = HmacSHA256(kRegion, serviceName);
        var signingKey = HmacSHA256(kService, aws4Request);
        return signingKey;
    }

    private  val hexArray = "0123456789ABCDEF".lowercase().toByteArray(StandardCharsets.US_ASCII);

    private fun bytesToHex(bytes:ByteArray): String  {
        var hexChars = ByteArray(bytes.size * 2);
        for (j in bytes.indices) {
            var v = bytes[j].toInt() and 0xFF;
            hexChars[j * 2] = hexArray[v.ushr(4)];
            hexChars[j * 2 + 1] = hexArray[v and 0x0F];
        }
        return String(hexChars,StandardCharsets.UTF_8)
    }

    private fun getTimeStamp():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//server timezone
        return dateFormat.format(Date());
        //return "20240326T064455Z"
    }

    private fun getDate():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//server timezone
        return dateFormat.format(Date());
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