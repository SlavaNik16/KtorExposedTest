package com.example.Services.Mapper.Implementation


import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.CardModel
import com.example.Models.UserModel
import com.example.Services.Mapper.ProfileMapper
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.firstValue
import java.sql.ResultSet

class Mapper : ProfileMapper() {
    override fun mapToUserModel(row: ResultRow?): UserModel? {
        if(row == null){
            return null
        }
        var userModel = UserModel(
            surname = row[UserTable.surname],
            name =  row[UserTable.name],
            email =  row[UserTable.email],
            password = row[UserTable.password],
            roleType = row[UserTable.roleType],
            statusType = row[UserTable.statusType],
        )
        return userModel
    }
}