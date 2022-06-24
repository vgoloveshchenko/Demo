package com.example.demo.presentation.extension

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.example.demo.domain.model.settings.Language
import java.util.*

fun Context.applySelectedAppLanguage(language: Language): Context {
    val newConfig = Configuration(resources.configuration)
    Locale.setDefault(language.locale)
    newConfig.setLocale(language.locale)

    return createConfigurationContext(newConfig)
}

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED