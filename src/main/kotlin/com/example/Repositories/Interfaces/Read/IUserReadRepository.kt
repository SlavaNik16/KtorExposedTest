package com.example.Repositories.Interfaces.Read

import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel

/**
 * Репозиторий чтения UserTable
 */
interface IUserReadRepository{
    suspend fun getUserByEmail(email:String): UserModel?
}