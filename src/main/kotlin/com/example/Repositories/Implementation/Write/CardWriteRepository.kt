package com.example.Repositories.Implementation.Write

import com.example.Context.Database.Tables.Models.CardTable
import com.example.Repositories.Implementation.BaseWriteRepository
import com.example.Repositories.Interfaces.Write.ICardWriteRepository

class CardWriteRepository: BaseWriteRepository<CardTable>(), ICardWriteRepository {

}