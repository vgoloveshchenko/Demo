package com.example.demo.domain.service

import com.example.demo.domain.model.settings.Language
import kotlinx.coroutines.flow.Flow

interface LanguageService {

    var language: Language
    val languageFlow: Flow<Language>
}