package com.example.Models.Request

import com.example.Context.Database.Tables.Enum.RoleTypes
import com.example.Context.Database.Tables.Enum.StatusTypes
import com.example.Utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class RegisterRequest (
    var surname: String,
    var name: String,
    var email: String,
    var password: String,
)