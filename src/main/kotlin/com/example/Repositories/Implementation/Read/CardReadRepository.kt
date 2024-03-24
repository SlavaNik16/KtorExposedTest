package com.example.Repositories.Implementation.Read

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Context.Database.Tables.Results.CardTableResult
import com.example.Context.Database.Tables.Results.UserTableResult
import com.example.Models.CardModel
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.Read.ICardReadRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class CardReadRepository : ICardReadRepository {
    override suspend fun getAll(): List<CardTableResult> {
        return dbQuery{
            CardTable.selectAll()
                .mapNotNull {
                    rowToCard(it)
                }.toList()
        }
    }

    override suspend fun getById(id: UUID): CardTableResult? {
        return dbQuery{
            CardTable.selectAll()
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
            cardTable = row as CardTable
        )
        return cardTableResult
    }
}