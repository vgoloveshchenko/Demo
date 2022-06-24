package com.example.demo.data.di

import com.example.demo.data.service.location.LocationService
import com.example.demo.data.service.preferences.PreferencesService
import com.example.demo.domain.service.LanguageService
import com.example.demo.domain.service.NightModeService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val serviceModule = module {
    singleOf(::PreferencesService) {
        bind<NightModeService>()
        bind<LanguageService>()
    }
    singleOf(::LocationService)
}