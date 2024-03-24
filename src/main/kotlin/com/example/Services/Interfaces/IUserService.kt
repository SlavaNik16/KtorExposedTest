package com.example.Services.Interfaces

import com.example.Models.UserModel
import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import org.koin.ktor.ext.inject
import java.util.*

interface IUserService {

    suspend fun getUserByEmail(email:String): UserModel?
    suspend fun create(userModel: UserModel)

    //fun generateAccessToken(userModel: UserModel):String
}