package com.example.demo.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(
        networkModule,
        databaseModule,
        repositoryModule,
        serviceModule,
        useCaseModule
    )
}