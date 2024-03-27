package com.example.Services.Authentication.AWS

import io.ktor.http.*
import java.util.*

class AwsBuilder {

    private var accessKeyID: String
    var AccessKeyID: String
        get() = accessKeyID
        private set(value) {
            accessKeyID = value
        }
    private var secretAccessKey: String
    var SecretAccessKey: String
        get() = secretAccessKey
        private set(value) {
            secretAccessKey = value
        }
    private var regionName: String = "spb"
    var RegionName: String
        get() = regionName
        set(value) {
            regionName = value
        }
    private var serviceName: String = "s3"
    var ServiceName: String
        get() = serviceName
        set(value) {
            serviceName = value
        }
    private var httpMethodName: HttpMethod = HttpMethod.Get
    var HTTPMethod: HttpMethod
        get() = httpMethodName
        set(value) {
            httpMethodName = value
        }
    private var canonicalURI: String? = null
    var CanonicalURI: String?
        get() {
            return if(canonicalURI == null || canonicalURI?.trim()!!.isEmpty()) "/" else canonicalURI
        }
        set(value) {
            canonicalURI = value
        }
    private var queryParametes: TreeMap<String, String> = TreeMap()

    var QueryParametes: TreeMap<String, String>
        get() = queryParametes
        set(value) {
            queryParametes = value
        }

    private var awsHeaders: TreeMap<String, String> = TreeMap()
    var AwsHeaders: TreeMap<String, String>
        get() = awsHeaders
        set(value) {
            awsHeaders = value
        }
    private var payload: String? = null
    var Payload: String?
        get() {
            return if (payload == null) "" else payload
        }
        set(value) {
            payload = value
        }

    private var debug = false
    var Debug: Boolean
        get() = debug
        set(value) {
            debug = value
        }

    constructor(accessKeyID: String, secretAccessKey: String) {
        this.accessKeyID = accessKeyID;
        this.secretAccessKey = secretAccessKey;
    }

    fun initRegionName(regionName: String): AwsBuilder {
        this.regionName = regionName;
        return this;
    }

    fun initServiceName(serviceName: String): AwsBuilder {
        this.serviceName = serviceName;
        return this;
    }
    fun initHTTPMethod(httpMethodName: HttpMethod): AwsBuilder {
        this.httpMethodName = httpMethodName;
        return this;
    }

    fun initCanonicalURI(canonicalURI: String?): AwsBuilder {
        this.canonicalURI = canonicalURI;
        return this;
    }

    fun initQueryParametes(queryParametes: TreeMap<String, String>): AwsBuilder {
        this.queryParametes = queryParametes;
        return this;
    }

    fun initAwsHeaders(awsHeaders: TreeMap<String, String>): AwsBuilder {
        this.awsHeaders = awsHeaders;
        return this;
    }

    fun initPayload(payload: String?): AwsBuilder {
        this.payload = payload;
        return this;
    }

    fun initDebug(): AwsBuilder {
        this.debug = true;
        return this;
    }

    fun build(): AWSV4Auth {
        return AWSV4Auth(this);
    }
}