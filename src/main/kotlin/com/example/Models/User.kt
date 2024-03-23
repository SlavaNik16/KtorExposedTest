package com.example.Models

import java.util.UUID

class User(
    val name: String,
    val age: Int,
    val cityId:UUID,
) : BaseAuditEntity()