package com.example.Registrations.DependencyInjection

import com.example.Context.Database.CommonEntity.Providers.DateTimeProvider
import com.example.Context.Database.CommonEntity.Providers.IDateTimeProvider
import com.example.Registrations.Mapper.Implementation.Mapper
import com.example.Registrations.Mapper.ProfileMapper
import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Implementation.Write.UserWriteRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Repositories.Interfaces.Write.IUserWriteRepository
import com.example.Services.Authentication.IJWTService
import com.example.Services.Authentication.JWTService
import com.example.Services.Implementations.UserService
import com.example.Services.Interfaces.IUserService
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userModule = module {
    factory<IDateTimeProvider> { DateTimeProvider() }

    factory<IUserReadRepository> { UserReadRepository() }
    factory<IUserWriteRepository> { UserWriteRepository() }
    factory<IJWTService> { JWTService() }
    factory<ProfileMapper> { Mapper() }
    factoryOf(::UserService).bind(IUserService::class)
}