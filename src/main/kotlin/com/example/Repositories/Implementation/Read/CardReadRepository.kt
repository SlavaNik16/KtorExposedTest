package com.example.Repositories.Implementation.Read

import com.example.Context.Database.CommonEntity.notDeletedAt
import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Results.CardTableResult
import com.example.Repositories.Interfaces.Read.ICardReadRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class CardReadRepository : ICardReadRepository {
    override suspend fun getAll(): List<CardTableResult> {
        return dbQuery{
            CardTable.selectAll()
                .notDeletedAt(CardTable)
                .mapNotNull {
                    rowToCard(it)
                }.toList()
        }
    }

    override suspend fun getById(id: UUID): CardTableResult? {
        return dbQuery{
            CardTable.selectAll()
                .notDeletedAt(CardTable)
                .where(CardTable.id.eq(id))
                .map {
                    rowToCard(it)
                }.singleOrNull()
        }
    }

    private fun rowToCard(row: ResultRow?): CardTableResult?{
        if(row == null){
            return null
        }
        var cardTableResult = CardTableResult(
            resultRow = row,
            cardTableId = row[CardTable.id]
        )
        return cardTableResult
    }
}