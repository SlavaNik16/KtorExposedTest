package com.example.Repositories.Interfaces.Read

import com.example.Models.CardModel
import com.example.Models.UserModel
import java.util.UUID

/**
 * Репозиторий чтения CardTable
 */
interface ICardReadRepository {

    /**
     * Получение всех карточек
     */
    suspend fun getAll(): List<CardModel>

    /**
     * Получение карточки по идентификатору
     */
    suspend fun getById(id:UUID): CardModel?
}