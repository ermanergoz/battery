package com.erman.battery.ui.main

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.erman.battery.data.ChargingMode
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Rule
import org.junit.Test

class BatteryGaugeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsThePercentage() {
        composeTestRule.setContent { BatteryTheme { BatteryGauge(61, ChargingMode.USB) } }

        composeTestRule.onNodeWithText("61%").assertExists()
    }

    @Test
    fun describesTheLevelAndPowerSourceForScreenReaders() {
        composeTestRule.setContent { BatteryTheme { BatteryGauge(61, ChargingMode.USB) } }

        composeTestRule.onNodeWithContentDescription("Battery at 61%, Charging via USB").assertExists()
    }
}
