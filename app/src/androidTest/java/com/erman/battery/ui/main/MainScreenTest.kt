package com.erman.battery.ui.main

import android.os.BatteryManager
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.erman.battery.data.BatteryStatus
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val status = BatteryStatus.from(
        level = 61, scale = 100, voltage = 3989, temperatureTenths = 260,
        plugged = BatteryManager.BATTERY_PLUGGED_USB,
        health = BatteryManager.BATTERY_HEALTH_GOOD,
        status = BatteryManager.BATTERY_STATUS_CHARGING,
        technology = "Li-ion"
    )

    @Test
    fun showsEverySectionOnceTheBatteryIsRead() {
        setScreen(status)

        composeTestRule.onNodeWithText("61%").assertExists()
        composeTestRule.onNodeWithText("357 mA").assertExists()
        composeTestRule.onNodeWithText("Charge alarm").performScrollTo().assertExists()
        composeTestRule.onNodeWithText("Battery usage").performScrollTo().assertExists()
    }

    @Test
    fun waitsForTheFirstReading() {
        setScreen(status = null)

        composeTestRule.onNodeWithText("Charge alarm").assertDoesNotExist()
    }

    @Test
    fun settingsActionOpensSettings() {
        var opened = false
        setScreen(status, onSettingsClick = { opened = true })

        composeTestRule.onNodeWithContentDescription("Settings").performClick()

        assertTrue(opened)
    }

    private fun setScreen(status: BatteryStatus?, onSettingsClick: () -> Unit = {}) {
        composeTestRule.setContent {
            BatteryTheme {
                MainScreen(
                    status = status,
                    current = 357421,
                    isAlarmEnabled = false,
                    limitText = "80",
                    isLimitInvalid = false,
                    onAlarmToggled = {},
                    onLimitChanged = {},
                    onUsageClick = {},
                    onSettingsClick = onSettingsClick
                )
            }
        }
    }
}
