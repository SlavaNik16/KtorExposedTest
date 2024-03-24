package com.example.Repositories.Implementation.Read

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.Read.IUserReadRepository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

import java.util.*

class UserReadRepository : IUserReadRepository {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return dbQuery {
            UserTable
                .selectAll().where(UserTable.email.eq(email))
                .map{
                    rowToUser(row = it)
                }
                .singleOrNull()
        }
    }

    private fun rowToUser(row:ResultRow?):UserModel?{
        if(row == null){
            return null
        }
        var user = UserModel(
            id = row[UserTable.id],
            surname = row[UserTable.surname],
            name = row[UserTable.name],
            email = row[UserTable.email],
            password = row[UserTable.password],
            roleType = row[UserTable.roleType],
            statusType = row[UserTable.statusType],
        )
        return user
    }
}