package com.example.Context.Database.CommonEntity.EntityInterface

import org.jetbrains.exposed.sql.Column
import java.time.OffsetDateTime

interface IEntityAuditUpdated {

    /**
     * Дата обновления
     */
    var updatedAt: Column<OffsetDateTime>

    /**
     * Кто обновил
     */
    var updatedBy: Column<String>

}