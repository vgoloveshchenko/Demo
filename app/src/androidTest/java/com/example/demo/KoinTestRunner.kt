package com.example.demo

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * The class provides base [Application] and used as instrumentation test runner. Allows to create
 * only necessary dependencies and invoke [org.koin.core.context.startKoin] manually.
 */
@Suppress("unused")
class KoinTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, Application::class.java.name, context)
    }
}