package com.example.Context.Database.Tables.Results

import org.jetbrains.exposed.sql.ResultRow
import java.util.*

data class CardTableResult(
    val resultRow: ResultRow,
    val cardTableId: UUID,
)