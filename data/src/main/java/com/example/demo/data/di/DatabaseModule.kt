package com.example.demo.data.di

import androidx.room.Room
import com.example.demo.data.database.AppDatabase
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        Room
            .databaseBuilder(
                get(),
                AppDatabase::class.java,
                "app_database.db"
            )
            .build()
    }

    single { get<AppDatabase>().userDao() }
}