package com.example.demo.presentation.ui.nightmode

import androidx.lifecycle.ViewModel
import com.example.demo.domain.service.NightModeService

class NightModeViewModel(private val prefsService: NightModeService) : ViewModel() {

    var selectedNightMode by prefsService::nightMode
}