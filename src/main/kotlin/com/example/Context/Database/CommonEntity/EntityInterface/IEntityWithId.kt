package com.example.Context.Database.CommonEntity.EntityInterface

import org.jetbrains.exposed.sql.Column
import java.util.*

interface IEntityWithId {
    /**
     * Уникальный идентификатор
     */
    val id: Column<UUID>
}