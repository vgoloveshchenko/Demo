package com.example.demo.presentation

import android.app.Application
import android.content.Context
import com.example.demo.data.di.dataModule
import com.example.demo.domain.service.LanguageService
import com.example.demo.presentation.di.viewModelModule
import com.example.demo.presentation.service.language.LanguageAwareContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DemoApp : Application() {

    private val appScope = MainScope()

    private val languageService by inject<LanguageService>()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageAwareContext(base, application = this))
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DemoApp)
            modules(
                dataModule,
                viewModelModule
            )
        }

        languageService
            .languageFlow
            .onEach {
                (baseContext as LanguageAwareContext).appLanguage = it
            }
            .launchIn(appScope)
    }
}