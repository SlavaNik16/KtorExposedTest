package com.example.Repositories.Implementation.Write

import com.example.Context.Database.Tables.Models.CardTable
import com.example.Context.Database.Tables.Models.UserTable
import com.example.Repositories.Implementation.BaseWriteRepository
import com.example.Repositories.Interfaces.Write.ICardWriteRepository
import com.example.Repositories.Interfaces.Write.IUserWriteRepository

class CardWriteRepository: BaseWriteRepository<CardTable>(), ICardWriteRepository {

}