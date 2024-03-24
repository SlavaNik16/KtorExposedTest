package com.example.Repositories.Interfaces.Read

import com.example.Context.Database.Tables.Results.CardTableResult
import java.util.*

/**
 * Репозиторий чтения CardTable
 */
interface ICardReadRepository {

    /**
     * Получение всех карточек
     */
    suspend fun getAll(): List<CardTableResult>

    /**
     * Получение карточки по идентификатору
     */
    suspend fun getById(id:UUID): CardTableResult?
}