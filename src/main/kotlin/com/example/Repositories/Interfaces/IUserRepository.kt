package com.example.Repositories.Interfaces

import com.example.Models.UserModel

interface IUserRepository {

    suspend fun getUserByEmail(email:String):UserModel?

    suspend fun create(userModel: UserModel)
}