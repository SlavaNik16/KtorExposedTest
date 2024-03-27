package com.example.Services.Interfaces

import com.example.Models.UserModel
import java.util.*

/**
 * Сервис UserTable
 */
interface IUserService {

    suspend fun getAll(): List<UserModel>
    suspend fun getUserByEmail(email:String): UserModel?

    suspend fun getUserById(id:UUID): UserModel?

    suspend fun create(userModel: UserModel)

    fun generateAccessToken(userModel: UserModel):String
}