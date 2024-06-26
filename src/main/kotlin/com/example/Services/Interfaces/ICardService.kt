package com.example.Services.Interfaces

import com.example.Models.CardModel
import java.util.*


/**
 * Сервис CardTable
 */
interface ICardService {

    /**
     * Получение всех карточек
     */
    suspend fun getAll(): List<CardModel>

    /**
     * Получение карточки по идентификатору
     */
    suspend fun getById(id: UUID): CardModel?

    /**
     * Получение карточки
     */
    suspend fun addCard(cardModel: CardModel): CardModel?

    /**
     * Обновления карточки
     */
    suspend fun updateCardUserId(cardModel: CardModel, userId: UUID): CardModel?

    /**
     * Удаление карточки
     */
    suspend fun deleteCard(id: UUID)

}