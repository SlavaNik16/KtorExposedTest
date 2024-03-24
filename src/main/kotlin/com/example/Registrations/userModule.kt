package com.example.Registrations

import com.example.Repositories.Implementation.Read.UserReadRepository
import com.example.Repositories.Implementation.Write.UserWriteRepository
import com.example.Repositories.Interfaces.Read.IUserReadRepository
import com.example.Repositories.Interfaces.Write.IUserWriteRepository
import com.example.Services.Implementations.UserService
import com.example.Services.Interfaces.IUserService
import org.koin.dsl.module

val userModule = module {
    single<IUserReadRepository> { UserReadRepository() }
    single<IUserWriteRepository> { UserWriteRepository() }
    single<IUserService> { UserService(get(), get()) }
}