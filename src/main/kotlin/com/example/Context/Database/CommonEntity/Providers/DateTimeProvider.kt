package com.example.Context.Database.CommonEntity.Providers

import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateTimeProvider : IDateTimeProvider {
    override fun UtcNow(): OffsetDateTime {
        return OffsetDateTime.now(ZoneOffset.UTC)
    }
}