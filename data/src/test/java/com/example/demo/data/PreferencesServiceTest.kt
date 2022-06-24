package com.example.demo.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.demo.data.service.preferences.PreferencesService
import com.example.demo.domain.model.settings.Language
import com.example.demo.domain.model.settings.NightMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PreferencesServiceTest {

    private lateinit var prefsService: PreferencesService

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        prefsService = PreferencesService(context)
    }

    @Test
    fun `test default values`() {
        Assert.assertEquals(NightMode.SYSTEM, prefsService.nightMode)
        Assert.assertEquals(Language.EN, prefsService.language)
    }

    @Test
    fun `test default language flow`() = runTest {
        Assert.assertEquals(Language.EN, prefsService.languageFlow.first())
    }

    @Test
    fun `test changed language flow`() = runTest {
        prefsService.language = Language.RU
        Assert.assertEquals(Language.RU, prefsService.languageFlow.first())
    }
}