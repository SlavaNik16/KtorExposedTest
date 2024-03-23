package com.example.Models

import io.ktor.utils.io.core.*
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

abstract class BaseAuditEntity {
    /**
     * Уникальный идентификатор
     */
    var Id: UUID = UUID.randomUUID()

    /**
     * Дата создания
     */
    var CreatedAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)

    /**
     * Кто создал
     */
    var CreatedBy: String = String()

    /**
     * Дата обновления
     */
    var UpdatedAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)

    /**
     * Кто обновил
     */
    var UpdatedBy: String = String()

    /**
     * Дата удаления
     */
    var DeletedAt: OffsetDateTime? = null
}