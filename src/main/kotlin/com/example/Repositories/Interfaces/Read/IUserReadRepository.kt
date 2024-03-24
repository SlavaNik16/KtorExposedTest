package com.example.Repositories.Interfaces.Read

import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

/**
 * Репозиторий чтения UserTable
 */
interface IUserReadRepository{
    suspend fun getUserByEmail(email:String): ResultRow?

    suspend fun getUserById(id:UUID): ResultRow?
}