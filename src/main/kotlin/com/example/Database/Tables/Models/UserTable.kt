package com.example.Database.Tables.Models

import com.example.Database.Tables.Enum.RoleTypes
import com.example.Database.Tables.Enum.StatusTypes
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object UserTable : BaseAuditEntity() {
    val surname: Column<String> = varchar("surname",80)
    val name: Column<String> = varchar("name",80)
    val email: Column<String>  = varchar("email", 1000).uniqueIndex()
    val password:Column<String> =varchar("password", 50)
    val cityId: Column<UUID> = uuid("cityId")
    val roleType: Column<RoleTypes> = enumeration("roleType")
    val statusType:Column<StatusTypes> = enumeration("statusType")
}