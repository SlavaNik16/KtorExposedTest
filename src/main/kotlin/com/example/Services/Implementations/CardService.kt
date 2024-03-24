package com.example.Services.Implementations

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.CardTable
import com.example.Models.CardModel
import com.example.Repositories.Interfaces.Read.ICardReadRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Repositories.Interfaces.Write.ICardWriteRepository
import com.example.Services.Interfaces.ICardService
import com.example.Services.Mapper.ProfileMapper
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import java.util.*

class CardService(
    private val cardReadRepository: ICardReadRepository,
    private val cardWriteRepository: ICardWriteRepository,
    private val userReadRepository: IUserReadRepository,
    private val profileMapper: ProfileMapper,
):ICardService {
    override suspend fun getAll(): List<CardModel>{
        var cardResult = cardReadRepository.getAll()
        return profileMapper.mapToCardsModel(cardResult)
    }


    override suspend fun getById(id: UUID): CardModel?{
        var cardResult = cardReadRepository.getById(id)
        return profileMapper.mapToCardModel(cardResult)
    }

    override suspend fun addCard(cardModel: CardModel): CardModel? {

        var user = userReadRepository.getUserById(cardModel.userId)
        if(user == null){
            //Пользователь не существует
            return null
        }

        var card = dbQuery {
             CardTable.insert { table ->
                table[id] = cardModel.id
                table[userId] = cardModel.userId
                table[title] = cardModel.title
                table[description] = cardModel.description
                table[isVerified] = cardModel.isVerified
            }
        }
//        cardWriteRepository.add(
//            entity = card.table as CardTable,
//            createdBy = "${user.surname} ${user.name}"
//        )
        return cardModel
    }

    override suspend fun updateCard(cardModel: CardModel, userId: UUID): CardModel? {

        var user = userReadRepository.getUserById(userId)
        if(user == null){
            //Пользователь не существует
            return null
        }
        dbQuery {
            CardTable.update(
                where = {
                    CardTable.userId.eq(userId) and CardTable.id.eq(cardModel.id)
                }
            ) { table ->
                table[id] = cardModel.id
                table[CardTable.userId] = userId
                table[title] = cardModel.title
                table[description] = cardModel.description
                table[isVerified] = cardModel.isVerified
            }
        }

        var card = cardReadRepository.getById(cardModel.id)

//        cardWriteRepository.update(
//            card,"${user.surname} ${user.name}"
//        )
        return cardModel
    }

    override suspend fun deleteCard(id: UUID, userId: UUID) {
        TODO("Not yet implemented")
    }

}