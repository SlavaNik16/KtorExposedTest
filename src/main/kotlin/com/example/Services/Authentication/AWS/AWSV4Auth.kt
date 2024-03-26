package com.example.Services.Authentication.AWS

import java.net.URLEncoder
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toCharArray

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


        var accessKeyID: String = "AKIA5TRHZWO3QGECXEHC"
        var secretAccessKey: String = "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"
        var regionName: String =  "spb"
        var serviceName: String = "s3"
        var httpMethodName: String = "GET"
        var canonicalURI: String = "127.0.0.1:8080"
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

        fun payload(payload:String): Builder
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


    private var accessKeyID: String = "AKIA5TRHZWO3QGECXEHC"
    private var secretAccessKey: String = "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"
    private var regionName: String =  "spb"
    private var serviceName: String = "s3"
    private var httpMethodName: String = "GET"
    private var canonicalURI: String = "127.0.0.1:8080"
    private var queryParametes: TreeMap<String, String>
    private var awsHeaders: TreeMap<String, String>
    private var payload: String? = null
    private var debug = false
    private var strSignedHeader: String = "/s3/validate"
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

    /**
     * Task 2: Create a signature version 4 of the string to be signed
     *
     * @param canonicalURL
     * @return
     */
    private fun prepareStringToSign(canonicalURL: String): String {
        var stringToSign = ""


        /* Step 2.1 Start with the algorithm name and wrap. */
        stringToSign = HMACAlgorithm + "\n"


        /* Step 2.2 Add date and wrap. */
        stringToSign += xAmzDate + "\n"


        /* Step 2.3 Add the certification scope and wrap. */
        stringToSign += ((currentDate + "/" + regionName).toString() + "/" + serviceName + "/" + aws4Request).toString() + "\n"


        /* Step 2.4 Add the canonical URL hash result returned by task 1, and then wrap the line. */
        stringToSign += generateHex(canonicalURL)

        if (debug) {
            println("##String to sign:\n$stringToSign")
        }

        return stringToSign
    }

    /**
     * Task 3: Calculate signatures for AWS Signature Version 4
     *
     * @param stringToSign
     * @return
     */
    private fun calculateSignature(stringToSign: String): String? {
        try {
            /* Step 3.1 Generate the signature key */
            val signatureKey: ByteArray = getSignatureKey(secretAccessKey, currentDate, regionName, serviceName)


            /* Step 3.2 Calculate the signature. */
            val signature = HmacSHA256(signatureKey, stringToSign)


            /* Step 3.2.1 Processing the signature code */
            val strHexSignature = bytesToHex(signature)
            return strHexSignature
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * Task 4: Add signature information to the request and return headers
     *
     * @return
     */
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
        return (((((HMACAlgorithm + " "
                + "Credential=" + accessKeyID).toString() + "/" + getDate() + "/" + regionName).toString() + "/" + serviceName + "/" + aws4Request).toString() + ", "
                + "SignedHeaders=" + strSignedHeader).toString() + ", "
                + "Signature=" + strSignature)
    }

    fun generateHex(data:String):String{
        var digest  = MessageDigest.getInstance("SHA-256")
        digest.update(data.toByteArray(Charsets.UTF_8))
        var digests: ByteArray = digest.digest()
        return with(StringBuilder()){
            digests.forEach { b-> append(String.format("%064X", b)) }
            toString().lowercase()
        }
    }

    fun HmacSHA256(key:ByteArray, data:String):ByteArray{
        val algorithm = "HmacSHA256"
        val hmac = Mac.getInstance(algorithm)
        hmac.init(SecretKeySpec(key, algorithm))
        return hmac.doFinal(data.toByteArray(Charsets.UTF_8))
    }


    fun getSignatureKey(key:String,  date:String, regionName:String, serviceName:String):ByteArray
    {
        var KSecret = ("AWS4".plus(key)).toByteArray(Charsets.UTF_8)
        var kDate = HmacSHA256(KSecret,date)
        var kRegion = HmacSHA256(kDate, regionName);
        var kService = HmacSHA256(kRegion, serviceName);
        var signingKey = HmacSHA256(kService, aws4Request);
        return signingKey;
    }

    private  val hexArray = "0123456789ABCDEF".lowercase().toCharArray();

    private fun bytesToHex(bytes:ByteArray): String  {
        var hexChars = CharArray(bytes.size * 2);
        for (j in bytes.indices) {
            var v = bytes[j].toInt() and 0xFF;
            hexChars[j * 2] = hexArray[v.ushr(4)];
            hexChars[j * 2 + 1] = hexArray[v and 0x0F];
        }
        return String(hexChars)
    }

    private fun getTimeStamp():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//server timezone
        return dateFormat.format(Date());
    }

    private fun getDate():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//server timezone
        return dateFormat.format(Date());
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