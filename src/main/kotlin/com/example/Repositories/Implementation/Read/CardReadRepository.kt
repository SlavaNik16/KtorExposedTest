package com.example.Repositories.Implementation.Read

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.CardModel
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.Read.ICardReadRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class CardReadRepository : ICardReadRepository {
    override suspend fun getAll(): List<CardModel> {
        return dbQuery{
            CardTable.selectAll()
                .mapNotNull {
                    rowToCard(it)
                }.toList()
        }
    }

    override suspend fun getById(id: UUID): CardModel? {
        return dbQuery{
            CardTable.selectAll()
                .where(CardTable.id.eq(id))
                .map {
                    rowToCard(it)
                }.singleOrNull()
        }
    }

    private fun rowToCard(row: ResultRow?): CardModel?{
        if(row == null){
            return null
        }
        var card = CardModel(
        userId = row[CardTable.userId],
        title = row[CardTable.title],
        description = row[CardTable.description],
        createdAt = row[CardTable.createdAt],
        isVerified = row[CardTable.isVerified],
        )
        return card
    }

//    private fun rowToCard(row: ResultRow?): CardTable?{
//        if(row == null){
//            return null
//        }
//        return row as CardTable
//    }
}