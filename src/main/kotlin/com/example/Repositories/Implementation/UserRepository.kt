package com.example.Repositories.Implementation

import com.example.Database.DatabaseFactory.dbQuery
import com.example.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.IUserRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository : IUserRepository{
    override suspend fun getUserByEmail(email: String): UserModel? {
        return dbQuery {
            UserTable.select(UserTable.email.eq(email))
                .map { UserModel(surname = it[UserTable.surname], name = it[UserTable.name], email = it[UserTable.email], password = it[UserTable.password]) }
                .singleOrNull()
        }
    }

    override suspend fun create(userModel: UserModel) {
        TODO("Not yet implemented")
    }

}