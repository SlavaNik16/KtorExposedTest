package com.example.Models

import com.example.Context.Database.CommonEntity.Providers.DateTimeProvider
import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import com.example.Utils.OffsetDateTimeSerializer
import com.example.Utils.UUIDSerializer
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.*

@Serializable
data class CardModel (
    @Serializable(with = UUIDSerializer::class)
    var id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val title: String,
    val description: String?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createdAt: OffsetDateTime = DateTimeProvider().UtcNow(),
    val isVerified:Boolean = false,
)