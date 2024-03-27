package com.example.Context.Database.Tables.Models

import com.example.Context.Database.CommonEntity.EntityInterface.*
import com.example.Context.Database.CommonEntity.Providers.DateTimeProvider
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import java.time.OffsetDateTime
import java.util.*

abstract class BaseAuditEntity : Table(),
    IEntity,
    IEntityWithId,
    IEntityAuditCreated,
    IEntityAuditUpdated,
    IEntityAuditDeleted {

    /**
     * Уникальный идентификатор
     */
    override val id: Column<UUID> = uuid("id").autoGenerate()

    /**
     * Дата создания
     */
    override val createdAt: Column<OffsetDateTime> =
        timestampWithTimeZone("createdAt").default(DateTimeProvider().UtcNow())

    /**
     * Кто создал
     */
    override val createdBy: Column<String> = varchar("createdBy", 200).default("ApiExampleApp")

    /**
     * Дата обновления
     */
    override val updatedAt: Column<OffsetDateTime> =
        timestampWithTimeZone("updatedAt").default(DateTimeProvider().UtcNow())

    /**
     * Кто обновил
     */
    override val updatedBy: Column<String> = varchar("updatedBy", 200).default("ApiExampleApp")

    /**
     * Дата удаления
     */
    override val deletedAt: Column<OffsetDateTime?> = timestampWithTimeZone("deletedAt").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}