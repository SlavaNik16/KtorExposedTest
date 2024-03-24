package com.example.Registrations.DependencyInjection

import com.example.Context.Database.CommonEntity.Providers.DateTimeProvider
import com.example.Context.Database.CommonEntity.Providers.IDateTimeProvider
import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Implementation.Write.UserWriteRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Repositories.Interfaces.Write.IUserWriteRepository
import com.example.Services.Authentication.IJWTService
import com.example.Services.Authentication.JWTService
import com.example.Services.Implementations.UserService
import com.example.Services.Interfaces.IUserService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userModule = module {
    factory<IDateTimeProvider> { DateTimeProvider() }

    single<IUserReadRepository> { UserReadRepository() }
    single<IUserWriteRepository> { UserWriteRepository() }
    single<IJWTService> { JWTService() }
    singleOf(::UserService).bind(IUserService::class)
}