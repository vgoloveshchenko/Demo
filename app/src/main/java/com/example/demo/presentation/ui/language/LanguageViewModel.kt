package com.example.demo.presentation.ui.language

import androidx.lifecycle.ViewModel
import com.example.demo.domain.model.settings.Language
import com.example.demo.domain.service.LanguageService

class LanguageViewModel(private val languageService: LanguageService) : ViewModel() {

    var selectedLanguage: Language by languageService::language
}