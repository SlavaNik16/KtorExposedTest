package com.example.Repositories.Implementation.Read

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class UserReadRepository : IUserReadRepository {

    override suspend fun getUserByEmail(email: String): ResultRow? {
        return dbQuery {
            UserTable
                .selectAll().where(UserTable.email.eq(email))
                .map{
                    it
                }
                .singleOrNull()
        }
    }

    override suspend fun getUserById(id: UUID): ResultRow? {
        return dbQuery {
            UserTable
                .selectAll().where(UserTable.id.eq(id))
                .map{
                    it
                }
                .singleOrNull()
        }
    }

//    private fun rowToUser(row:ResultRow?):UserModel?{
//        if(row == null){
//            return null
//        }
//        var user = UserModel(
//            id = row[UserTable.id],
//            surname = row[UserTable.surname],
//            name = row[UserTable.name],
//            email = row[UserTable.email],
//            password = row[UserTable.password],
//            roleType = row[UserTable.roleType],
//            statusType = row[UserTable.statusType],
//        )
//        return user
//    }

    private fun rowToUser(row: ResultRow?): ResultRow?{
        if(row == null){
            return null
        }
        return row
    }
}