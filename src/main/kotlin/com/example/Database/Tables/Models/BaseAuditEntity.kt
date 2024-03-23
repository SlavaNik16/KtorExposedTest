package com.example.Database.Tables.Models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import java.time.OffsetDateTime
import java.util.*

abstract class BaseAuditEntity : Table() {

    /**
     * Уникальный идентификатор
     */
    val id: Column<UUID> = uuid("id").autoGenerate()

    /**
     * Дата создания
     */
    val createdAt: Column<OffsetDateTime> = timestampWithTimeZone("createdAt")

    /**
     * Кто создал
     */
    val createdBy: Column<String> = varchar("createdBy",200)

    /**
     * Дата обновления
     */
    val updatedAt: Column<OffsetDateTime> = timestampWithTimeZone("updatedAt")

    /**
     * Кто обновил
     */
    val updatedBy: Column<String> = varchar("updatedBy",200)

    /**
     * Дата удаления
     */
    val deletedAt: Column<OffsetDateTime?> = timestampWithTimeZone("deletedAt").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}