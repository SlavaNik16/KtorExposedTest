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
    IEntityAuditDeleted
{

    /**
     * Уникальный идентификатор
     */
    override var id: Column<UUID> = uuid("id").autoGenerate()

    /**
     * Дата создания
     */
    override var createdAt: Column<OffsetDateTime> = timestampWithTimeZone("createdAt").default(DateTimeProvider().UtcNow())

    /**
     * Кто создал
     */
    override var createdBy: Column<String> = varchar("createdBy",200).default("ApiExampleApp")

    /**
     * Дата обновления
     */
    override var updatedAt: Column<OffsetDateTime> = timestampWithTimeZone("updatedAt").default(DateTimeProvider().UtcNow())

    /**
     * Кто обновил
     */
    override var updatedBy: Column<String> = varchar("updatedBy",200).default("ApiExampleApp")

    /**
     * Дата удаления
     */
    override var deletedAt: Column<OffsetDateTime?> = timestampWithTimeZone("deletedAt").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}