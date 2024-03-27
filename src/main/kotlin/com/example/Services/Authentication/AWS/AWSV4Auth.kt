package com.example.Services.Authentication.AWS

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.HmacUtils
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

    constructor(builder: AwsBuilder) {
        build = builder
    }

    private var build:AwsBuilder
    private var signedHeader:String = ""
    private var payloadHesh:String = ""
    private var xAmzDate: String = getTimeStamp()
    private var currentDate: String = getDate()


    /**
     * Создайние канонического запроса
     */
    private fun prepareCanonicalRequest(): String {
        val canonicalURL = StringBuilder("")
        canonicalURL.append(build.HTTPMethod.value).append("\n")
        canonicalURL.append(build.CanonicalURI).append("\n")
        val queryString = StringBuilder("")
        if (!build.QueryParametes.isEmpty()) {
            for ((key, value) in build.QueryParametes.entries) {
                queryString.append(URLEncoder.encode(key)).append("=").append(encodeParameter(URLEncoder.encode(value))).append("&")
            }
            queryString.deleteCharAt(queryString.lastIndexOf("&"))
        }
        queryString.append("\n")
        canonicalURL.append(queryString)

        val signedHeaders = StringBuilder("")
        if (!build.AwsHeaders.isEmpty()) {
            for ((key, value) in build.AwsHeaders.entries) {
                signedHeaders.append(key.lowercase()).append(";")
                canonicalURL.append(key.lowercase()).append(":").append(value.trim()).append("\n")
            }
        }
        canonicalURL.append("\n")

        signedHeader = signedHeaders.substring(0, signedHeaders.length - 1)
        canonicalURL.append(signedHeader).append("\n")
        canonicalURL.append(payloadHesh)
        if (build.Debug) {
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
                                (currentDate + "/" + build.RegionName + "/" + build.ServiceName + "/" + aws4Request) + "\n" +
                                generateHex(canonicalURL))
        if (build.Debug) {
            println("##String to sign:\n$stringToSign")
        }

        return stringToSign
    }

    private fun calculateSignature(stringToSign: String): String {
        val signatureKey = getSignatureKey(build.SecretAccessKey, currentDate, build.RegionName, build.ServiceName)
        val signature = HmacSHA256(signatureKey, stringToSign)
        val strHexSignature = bytesToHex(signature)
        return strHexSignature
    }

    fun getValidateStart(): String {
        build.AwsHeaders.put("x-amz-date", xAmzDate)
        payloadHesh = generateHex(build.Payload.toString())
        build.AwsHeaders.put("x-amz-content-sha256", payloadHesh)
        val canonicalURL = prepareCanonicalRequest()
        val stringToSign = prepareStringToSign(canonicalURL)
        val signature = calculateSignature(stringToSign)

        var resultToken=  buildAuthorizationString(signature)

        if (build.Debug) {
            println("\n##Signature:\n$signature")
            println("##Token:\n$resultToken")
            println("================================")
        }
        return resultToken
    }

    private fun buildAuthorizationString(strSignature: String): String {
        return (HMACAlgorithm + " "
                + "Credential=" + build.AccessKeyID + "/" + getDate() + "/" + build.RegionName + "/" + build.ServiceName + "/" + aws4Request + ", "
                + "SignedHeaders=" + signedHeader + ", "
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
    private fun generateHex(data:String):String{
        var digest  = MessageDigest.getInstance("SHA-256")
        digest.update(data.toByteArray(Charsets.UTF_8))
        var digests: ByteArray = digest.digest()
        return String.format("%064x", BigInteger(1, digests))
    }
    private fun HmacSHA256(key:ByteArray, data:String):ByteArray{
        val algorithm = "HmacSHA256"
        val hmac = Mac.getInstance(algorithm)
        hmac.init(SecretKeySpec(key, algorithm))
        return hmac.doFinal(data.toByteArray(Charsets.UTF_8))
    }


    private fun getSignatureKey(key:String,  date:String, regionName:String, serviceName:String):ByteArray
    {
        var KSecret = ("AWS4"+key).toByteArray()
        var kDate = HmacSHA256(KSecret,date)
        var kRegion = HmacSHA256(kDate, regionName);
        var kService = HmacSHA256(kRegion, serviceName);
        var signingKey = HmacSHA256(kService, aws4Request);
        return signingKey
    }

    private fun getTimeStamp():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        return dateFormat.format(Date())
    }

    private fun getDate():String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        return dateFormat.format(Date())
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