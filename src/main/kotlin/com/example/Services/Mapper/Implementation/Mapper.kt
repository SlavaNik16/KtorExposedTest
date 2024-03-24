package com.example.Services.Mapper.Implementation

import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Services.Mapper.ProfileMapper
import org.jetbrains.exposed.sql.ResultRow

class Mapper : ProfileMapper() {
    override fun map(row: ResultRow?): UserModel? {
        if(row == null){
            return null
        }
        var user = UserModel(
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