package com.example.Repositories.Interfaces.Read

import com.example.Context.Database.Tables.Results.UserTableResult
import java.util.*

/**
 * Репозиторий чтения UserTable
 */
interface IUserReadRepository {

    /**
     * Получение всех пользователей
     */
    suspend fun getAll(): List<UserTableResult>

    /**
     * Получение пользователя по почте
     */
    suspend fun getUserByEmail(email: String): UserTableResult?

    /**
     * Получение пользователя по идентификатору
     */
    suspend fun getUserById(id: UUID): UserTableResult?
}