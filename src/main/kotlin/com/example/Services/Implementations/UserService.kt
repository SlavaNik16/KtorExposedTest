package com.example.Services.Implementations

import com.example.Context.Database.Tables.Models.BaseAuditEntity
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Implementation.Write.UserWriteRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Repositories.Interfaces.Write.IUserWriteRepository
import com.example.Services.Authentication.JWTService
import com.example.Services.Interfaces.IUserService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.koin.java.KoinJavaComponent.inject

class UserService : IUserService {

    private val userReadRepository: IUserReadRepository by inject(UserReadRepository::class.java)
    private val userWriteRepository: IUserWriteRepository by inject(UserWriteRepository::class.java)
//    private val jwtService: JWTService = JWTService()
    override suspend fun getUserByEmail(email: String): UserModel? {
        var user: UserModel? = userReadRepository.getUserByEmail(email) ?: return null
        return user
    }
    override suspend fun create(userModel: UserModel) {
        var user = UserTable
        userWriteRepository.add(user, "${userModel.surname} ${userModel.name}")
        UserTable.insert{ table ->
            table[surname] = userModel.surname
            table[name] = userModel.name
            table[email] = userModel.email
            table[password] = userModel.password
            table[roleType] = userModel.roleType
            table[statusType] = userModel.statusType
//            table[createdAt] = user.createdAt
//            table[createdBy] = user.createdBy
//            table[updatedAt] = user.updatedAt
//            table[updatedBy] = user.updatedBy
//            table[deletedAt] = user.deletedAt
        }
    }
}