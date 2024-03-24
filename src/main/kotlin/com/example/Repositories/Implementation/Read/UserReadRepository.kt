package com.example.Repositories.Implementation.Read

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserReadRepository : IUserReadRepository {

    override suspend fun getUserByEmail(email: String): UserTable? {
        return dbQuery {
            UserTable.select(UserTable.email.eq(email))
                .map{
                    it as UserTable
                }
                .singleOrNull()
        }
    }

    override suspend fun create(userModel: UserModel) {
        return dbQuery {
            UserTable.insert { table ->
                table[surname] = userModel.surname
                table[name] = userModel.name
                table[email] = userModel.email
                table[password] = userModel.password
                table[roleType] = userModel.roleType
                table[statusType] = userModel.statusType
            }
        }
    }

}