package com.example.Models

import com.example.Context.Database.CommonEntity.Providers.DateTimeProvider
import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.CardTable.references
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Utils.UUIDSerializer
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import java.time.OffsetDateTime
import java.util.*


@Serializable
data class UserModel (
    @Serializable(with = UUIDSerializer::class)
    var id: UUID = UUID.randomUUID(),
    var surname: String,
    var name: String,
    var email: String,
    var password: String,
    var roleType: RoleTypes = RoleTypes.User,
    var statusType: StatusTypes = StatusTypes.Offline
): Principal