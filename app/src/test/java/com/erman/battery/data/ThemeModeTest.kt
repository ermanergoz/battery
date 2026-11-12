package com.erman.battery.data

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {

    @Test
    fun preferenceValueMapsToThemeMode() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromValue("system"))
        assertEquals(ThemeMode.LIGHT, ThemeMode.fromValue("light"))
        assertEquals(ThemeMode.DARK, ThemeMode.fromValue("dark"))
    }

    @Test
    fun missingOrUnknownValueFollowsTheSystem() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromValue(null))
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromValue("sepia"))
    }
}
