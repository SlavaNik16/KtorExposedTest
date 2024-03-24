package com.example.Context.Database.CommonEntity.EntityInterface

import org.jetbrains.exposed.sql.Column
import java.time.OffsetDateTime

interface IEntityAuditCreated {
    /**
     * Дата создания
     */
    val createdAt: Column<OffsetDateTime>

    /**
     * Кто создал
     */
    val createdBy: Column<String>
}