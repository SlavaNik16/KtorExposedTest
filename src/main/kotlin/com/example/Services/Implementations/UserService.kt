package com.example.Services.Implementations

import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Models.UserModel
import com.example.Registrations.Mapper.ProfileMapper
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Repositories.Interfaces.Write.IUserWriteRepository
import com.example.Services.Authentication.IJWTService
import com.example.Services.Interfaces.IUserService
import org.jetbrains.exposed.sql.insert
import java.util.*

class UserService(
    private val userReadRepository: IUserReadRepository,
    private val userWriteRepository: IUserWriteRepository,
    private val iJWTService: IJWTService,
    private val profileMapper: ProfileMapper
) : IUserService {
    override suspend fun getAll(): List<UserModel> {
        var userResult = userReadRepository.getAll()
        return profileMapper.mapToUsersModel(userResult)
    }

    override suspend fun getUserByEmail(email: String): UserModel? {
        var userResult = userReadRepository.getUserByEmail(email) ?: return null
        return profileMapper.mapToUserModel(userResult)
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        var userResult = userReadRepository.getUserById(id) ?: return null
        return profileMapper.mapToUserModel(userResult)
    }

    override suspend fun create(userModel: UserModel) {
       var user = dbQuery{
            UserTable.insert {table ->
                table[id] = userModel.id
                table[surname] = userModel.surname
                table[name] = userModel.name
                table[email] = userModel.email
                table[password] = userModel.password
                table[roleType] = userModel.roleType
                table[statusType] = userModel.statusType
            }
        }
        userWriteRepository.add(
            entity = user.table as UserTable,
            createdBy = "${userModel.surname} ${userModel.name}"
        )
    }
}