package com.example.demo.data

import android.content.SharedPreferences
import com.example.demo.data.service.preferences.PrefsDelegate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class PrefsDelegateTest {

    @Test
    fun `test delegate`() {
        val editor = mockk<SharedPreferences.Editor>(relaxUnitFun = true)
        val prefs = mockk<SharedPreferences> {
            every { edit() } returns editor
        }

        var backingField = 42
        var valueDelegate: Int by PrefsDelegate(
            sharedPrefs = prefs,
            getValue = {
                Assert.assertEquals(prefs, this)
                backingField
            },
            setValue = { newValue ->
                Assert.assertEquals(editor, this)
                Assert.assertEquals(1, newValue)
                backingField = newValue
            })

        Assert.assertEquals(42, valueDelegate)

        valueDelegate = 1
        Assert.assertEquals(1, valueDelegate)

        verify {
            editor.apply()
        }
    }
}