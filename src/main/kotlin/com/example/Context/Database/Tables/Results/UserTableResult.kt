package com.example.Context.Database.Tables.Results

import org.jetbrains.exposed.sql.ResultRow
import java.util.*

data class UserTableResult(
    val resultRow: ResultRow,
    val userTableId: UUID,
)