package com.example.Context.Database.CommonEntity.EntityInterface

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import java.time.OffsetDateTime

interface IEntityAuditUpdated {

    /**
     * Дата обновления
     */
    val updatedAt: Column<OffsetDateTime>

    /**
     * Кто обновил
     */
    val updatedBy: Column<String>

}