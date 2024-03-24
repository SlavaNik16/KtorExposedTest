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
    suspend fun addCard(card: CardModel): CardModel?

    /**
     * Обновления карточки
     */
    suspend fun updateCard(card: CardModel): CardModel?

    /**
     * Удаление карточки
     */
    suspend fun deleteCard(id: UUID, userId:UUID)

}