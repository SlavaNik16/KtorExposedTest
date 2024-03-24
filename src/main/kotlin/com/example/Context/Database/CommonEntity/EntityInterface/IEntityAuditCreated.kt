package com.example.Context.Database.CommonEntity.EntityInterface

import org.jetbrains.exposed.sql.Column
import java.time.OffsetDateTime

interface IEntityAuditCreated {
    /**
     * Дата создания
     */
    var createdAt: Column<OffsetDateTime>

    /**
     * Кто создал
     */
    var createdBy: Column<String>
}