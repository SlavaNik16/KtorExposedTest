package com.example.Services.Authentication

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*

object DateHelper {
    private const val ISO_8601_24H_FULL_FORMAT = "yyyyMMdd'T'HHmmss"
    fun Date.toIsoString(): String {
        val dateFormat: DateFormat = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
        return dateFormat.format(this)
    }
}