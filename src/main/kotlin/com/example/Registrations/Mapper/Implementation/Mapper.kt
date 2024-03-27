package com.example.Registrations.Mapper.Implementation


import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Context.Database.Tables.Results.CardTableResult
import com.example.Context.Database.Tables.Results.UserTableResult
import com.example.Models.CardModel
import com.example.Models.UserModel
import com.example.Registrations.Mapper.ProfileMapper

class Mapper : ProfileMapper() {
    override fun mapToUsersModel(rows: List<UserTableResult>): List<UserModel> {
        var usersModel: MutableList<UserModel> = mutableListOf()
        for (row in rows) {
            var row = row.resultRow
            var userModel = UserModel(
                id = row[UserTable.id],
                surname = row[UserTable.surname],
                name = row[UserTable.name],
                email = row[UserTable.email],
                password = row[UserTable.password],
                roleType = row[UserTable.roleType],
                statusType = row[UserTable.statusType],
            )
            usersModel.add(userModel)
        }
        return usersModel
    }

    override fun mapToUserModel(row: UserTableResult?): UserModel? {
        if (row == null) {
            return null
        }
        var row = row.resultRow
        var userModel = UserModel(
            id = row[UserTable.id],
            surname = row[UserTable.surname],
            name = row[UserTable.name],
            email = row[UserTable.email],
            password = row[UserTable.password],
            roleType = row[UserTable.roleType],
            statusType = row[UserTable.statusType],
        )
        return userModel
    }

    override fun mapToCardsModel(rows: List<CardTableResult>): List<CardModel> {
        var cardsModel: MutableList<CardModel> = mutableListOf()
        for (row in rows) {
            var row = row.resultRow
            var userModel = CardModel(
                id = row[CardTable.id],
                userId = row[CardTable.userId],
                title = row[CardTable.title],
                description = row[CardTable.description],
                createdAt = row[CardTable.createdAt],
                isVerified = row[CardTable.isVerified],
            )
            cardsModel.add(userModel)
        }
        return cardsModel

    }

    override fun mapToCardModel(row: CardTableResult?): CardModel? {
        if (row == null) {
            return null
        }
        var row = row.resultRow
        var userModel = CardModel(
            id = row[CardTable.id],
            userId = row[CardTable.userId],
            title = row[CardTable.title],
            description = row[CardTable.description],
            createdAt = row[CardTable.createdAt],
            isVerified = row[CardTable.isVerified],
        )
        return userModel
    }
}