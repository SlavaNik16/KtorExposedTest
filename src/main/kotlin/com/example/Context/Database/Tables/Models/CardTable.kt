package com.example.Context.Database.Tables.Models

import org.jetbrains.exposed.sql.Column
import java.util.*

object CardTable : BaseAuditEntity() {
    var userId: Column<UUID> = uuid("userId").references(UserTable.id)
    var title: Column<String> = varchar("title",80)
    var description: Column<String?> = varchar("description", 2000).nullable()
    var isVerified: Column<Boolean> = bool("isVerified")
}