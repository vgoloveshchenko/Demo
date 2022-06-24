package com.example.demo.data.di

import com.example.demo.domain.usecase.GetCountriesUseCase
import com.example.demo.domain.usecase.GetUserDetailsUseCase
import com.example.demo.domain.usecase.GetUsersUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val useCaseModule = module {
    singleOf(::GetUsersUseCase)
    singleOf(::GetUserDetailsUseCase)
    singleOf(::GetCountriesUseCase)
}