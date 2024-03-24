package com.example.Models

import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import com.example.Utils.UUIDSerializer
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserModel (
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val surname: String,
    val name: String,
    val email: String,
    val password: String,
    val roleType: RoleTypes = RoleTypes.User,
    val statusType: StatusTypes = StatusTypes.Offline
):Principal