package com.example.Repositories.Implementation.Read

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Context.Database.Tables.Results.UserTableResult
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class UserReadRepository : IUserReadRepository {

    override suspend fun getUserByEmail(email: String): UserTableResult? {
        return dbQuery {
            UserTable
                .selectAll().where(UserTable.email.eq(email))
                .map{
                    rowToUser(it)
                }
                .singleOrNull()
        }
    }

    override suspend fun getUserById(id: UUID): UserTableResult? {
        return dbQuery {
            UserTable
                .selectAll().where(UserTable.id.eq(id))
                .map{
                    rowToUser(it)
                }
                .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow?): UserTableResult?{
        if(row == null){
            return null
        }
        var userTableResult = UserTableResult(
            resultRow = row,
            userTable = row as UserTable
        )
        return userTableResult
    }
}