package com.example.Registrations

import com.example.Repositories.Implementation.Read.CardReadRepository
import com.example.Repositories.Implementation.Write.CardWriteRepository
import com.example.Repositories.Interfaces.Read.ICardReadRepository
import com.example.Repositories.Interfaces.Write.ICardWriteRepository
import com.example.Services.Implementations.CardService
import com.example.Services.Interfaces.ICardService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cardModule = module {
    single<ICardReadRepository> { CardReadRepository() }
    single<ICardWriteRepository> { CardWriteRepository() }
    singleOf(::CardService).bind(ICardService::class)
}