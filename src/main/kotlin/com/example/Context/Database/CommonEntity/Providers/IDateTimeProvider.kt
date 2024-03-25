package com.example.Context.Database.CommonEntity.Providers

import java.time.OffsetDateTime

interface IDateTimeProvider {

    fun UtcNow(): OffsetDateTime
}