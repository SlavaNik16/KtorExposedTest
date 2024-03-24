package com.example.Context.Database.Tables.Results

import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.UserTable
import org.jetbrains.exposed.sql.ResultRow

data class CardTableResult(
    val resultRow: ResultRow,
    val cardTable: CardTable,
)