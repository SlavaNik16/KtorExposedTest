package com.example.Services.Interfaces

import com.example.Models.UserModel

interface IUserService {

    suspend fun getUserByEmail(email:String): UserModel?
    suspend fun create(userModel: UserModel)

    //fun generateAccessToken(userModel: UserModel):String
}