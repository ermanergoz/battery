package com.erman.battery.ui.main

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.erman.battery.data.ChargeState
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BatteryDetailsCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsStatusAndTechnology() {
        composeTestRule.setContent { BatteryTheme { BatteryDetailsCard(ChargeState.FULL, "Li-poly", onUsageClick = {}) } }

        composeTestRule.onNodeWithText("Full").assertExists()
        composeTestRule.onNodeWithText("Li-poly").assertExists()
    }

    @Test
    fun missingTechnologyIsShownAsUnknown() {
        composeTestRule.setContent { BatteryTheme { BatteryDetailsCard(ChargeState.CHARGING, null, onUsageClick = {}) } }

        composeTestRule.onNodeWithText("Unknown").assertExists()
    }

    @Test
    fun usageButtonOpensBatteryUsage() {
        var clicked = false
        composeTestRule.setContent { BatteryTheme { BatteryDetailsCard(ChargeState.CHARGING, "Li-ion", onUsageClick = { clicked = true }) } }

        composeTestRule.onNodeWithText("Battery usage").performClick()

        assertTrue(clicked)
    }
}
