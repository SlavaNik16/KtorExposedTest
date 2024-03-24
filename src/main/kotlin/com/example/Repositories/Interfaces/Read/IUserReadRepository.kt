package com.example.Repositories.Interfaces.Read

import com.example.Context.Database.Tables.Results.UserTableResult
import java.util.*

/**
 * Репозиторий чтения UserTable
 */
interface IUserReadRepository{
    suspend fun getUserByEmail(email:String): UserTableResult?

    suspend fun getUserById(id:UUID): UserTableResult?
}