package com.erman.battery.ui.main

import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.erman.battery.data.ChargingMode
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Rule
import org.junit.Test

class ChargingSourceRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun onlyTheActiveSourceIsSelected() {
        composeTestRule.setContent { BatteryTheme { ChargingSourceRow(selected = ChargingMode.USB) } }

        composeTestRule.onNodeWithText("USB").assertIsSelected()
        composeTestRule.onNodeWithText("AC").assertIsNotSelected()
        composeTestRule.onNodeWithText("Wireless").assertIsNotSelected()
        composeTestRule.onNodeWithText("Battery").assertIsNotSelected()
    }
}
