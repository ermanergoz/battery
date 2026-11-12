package com.erman.battery.ui.settings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.erman.battery.data.ThemeMode
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var notificationChangedTo: Boolean? = null
    private var themeChangedTo: ThemeMode? = null
    private var backPressed = false

    private fun setScreen(themeMode: ThemeMode = ThemeMode.SYSTEM) {
        composeTestRule.setContent {
            BatteryTheme {
                SettingsScreen(
                    isOngoingNotificationEnabled = false,
                    themeMode = themeMode,
                    onOngoingNotificationChanged = { notificationChangedTo = it },
                    onThemeModeChanged = { themeChangedTo = it },
                    onBack = { backPressed = true }
                )
            }
        }
    }

    @Test
    fun tappingTheNotificationRowTurnsItOn() {
        setScreen()

        composeTestRule.onNodeWithText("Ongoing notification").performClick()

        assertEquals(true, notificationChangedTo)
    }

    @Test
    fun showsTheCurrentTheme() {
        setScreen(ThemeMode.LIGHT)

        composeTestRule.onNodeWithText("Light").assertExists()
    }

    @Test
    fun choosingAThemeInTheDialogReportsIt() {
        setScreen()

        composeTestRule.onNodeWithText("Theme").performClick()
        composeTestRule.onNodeWithText("Dark").performClick()

        assertEquals(ThemeMode.DARK, themeChangedTo)
        composeTestRule.onNodeWithText("Dark").assertDoesNotExist()
    }

    @Test
    fun backArrowLeavesTheScreen() {
        setScreen()

        composeTestRule.onNodeWithContentDescription("Navigate up").performClick()

        assertTrue(backPressed)
    }
}
