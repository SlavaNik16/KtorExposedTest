package com.example.Database.Tables.Models

import org.jetbrains.exposed.sql.Column
import java.util.*

object CardTable : BaseAuditEntity() {
    val userId: Column<UUID> = uuid("userId").references(UserTable.id)
    val title: Column<String> = varchar("title",80)
    val description: Column<String> = varchar("description", 2000)
}