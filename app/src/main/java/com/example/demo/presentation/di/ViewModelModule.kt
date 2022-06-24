package com.example.demo.presentation.di

import com.example.demo.presentation.ui.countries.CountriesViewModel
import com.example.demo.presentation.ui.users.details.UserDetailsViewModel
import com.example.demo.presentation.ui.language.LanguageViewModel
import com.example.demo.presentation.ui.nightmode.NightModeViewModel
import com.example.demo.presentation.ui.users.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::UsersViewModel)
    viewModelOf(::UserDetailsViewModel)
    viewModelOf(::NightModeViewModel)
    viewModelOf(::LanguageViewModel)
    viewModelOf(::CountriesViewModel)
}