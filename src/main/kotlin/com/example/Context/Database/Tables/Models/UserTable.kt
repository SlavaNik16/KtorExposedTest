package com.example.Context.Database.Tables.Models

import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

object UserTable : BaseAuditEntity() {
    var surname: Column<String> = varchar("surname", 80)
    var name: Column<String> = varchar("name", 80)
    var email: Column<String> = varchar("email", 1000)
    var password: Column<String> = varchar("password", 50)
    var roleType: Column<RoleTypes> = enumeration("roleType")
    var statusType: Column<StatusTypes> = enumeration("statusType")

    init {
        uniqueIndex("IX_${email.name}_${deletedAt.name}", email, deletedAt)
    }

}