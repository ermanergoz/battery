package com.erman.battery.ui.main

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.erman.battery.data.BatteryHealth
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Rule
import org.junit.Test

class BatteryStatsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsReadingsInFriendlyUnits() {
        composeTestRule.setContent { BatteryTheme { BatteryStats(357421, 3989f, 26f, BatteryHealth.GOOD) } }

        composeTestRule.onNodeWithText("357 mA").assertExists()
        composeTestRule.onNodeWithText("3.99 V").assertExists()
        composeTestRule.onNodeWithText("26.0 °C").assertExists()
        composeTestRule.onNodeWithText("Good").assertExists()
    }

    @Test
    fun unsupportedCurrentIsShownAsNotAvailable() {
        composeTestRule.setContent { BatteryTheme { BatteryStats(null, 3989f, 26f, BatteryHealth.GOOD) } }

        composeTestRule.onNodeWithText("N/A").assertExists()
    }
}
