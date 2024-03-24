package com.example.Context.Database.Tables.Results

import com.example.Context.Database.Tables.Models.UserTable
import org.jetbrains.exposed.sql.ResultRow

data class UserTableResult(
    val resultRow: ResultRow,
    val userTable: UserTable,
)