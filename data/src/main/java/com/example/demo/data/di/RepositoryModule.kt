package com.example.demo.data.di

import com.example.demo.data.repository.CountriesRepositoryImpl
import com.example.demo.data.repository.UserRepositoryImpl
import com.example.demo.domain.repository.CountryRepository
import com.example.demo.domain.repository.UserRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoryModule = module {
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::CountriesRepositoryImpl) { bind<CountryRepository>() }
}