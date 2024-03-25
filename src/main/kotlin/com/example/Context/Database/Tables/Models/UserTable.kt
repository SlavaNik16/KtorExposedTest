package com.example.Context.Database.Tables.Models

import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow

object UserTable : BaseAuditEntity() {
    val surname: Column<String> = varchar("surname", 80)
    val name: Column<String> = varchar("name", 80)
    val email: Column<String> = varchar("email", 1000)
    val password: Column<String> = varchar("password", 50)
    val roleType: Column<RoleTypes> = enumeration("roleType")
    val statusType: Column<StatusTypes> = enumeration("statusType")

    init {
        uniqueIndex(columns = arrayOf(email))
        {
            deletedAt.isNull()
        }
    }
}