package com.example.Models

import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import com.example.Utils.UUIDSerializer
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
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