package com.erman.battery.ui.main

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.erman.battery.ui.theme.BatteryTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChargeAlarmCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tappingTheRowTurnsTheAlarmOn() {
        var toggledTo: Boolean? = null
        composeTestRule.setContent {
            BatteryTheme { ChargeAlarmCard(false, "80", false, onToggle = { toggledTo = it }, onLimitChange = {}) }
        }

        composeTestRule.onNodeWithText("Charge alarm").performClick()

        assertEquals(true, toggledTo)
    }

    @Test
    fun limitIsLockedWhileTheAlarmIsOn() {
        composeTestRule.setContent {
            BatteryTheme { ChargeAlarmCard(true, "80", false, onToggle = {}, onLimitChange = {}) }
        }

        composeTestRule.onNode(hasSetTextAction()).assertIsNotEnabled()
        composeTestRule.onNodeWithText("Turn the alarm off to change the limit").assertExists()
    }

    @Test
    fun invalidLimitExplainsHowToFixIt() {
        composeTestRule.setContent {
            BatteryTheme { ChargeAlarmCard(false, "", true, onToggle = {}, onLimitChange = {}) }
        }

        composeTestRule.onNodeWithText("Enter a percentage between 1 and 100").assertExists()
    }

    @Test
    fun typedLimitIsReported() {
        var typed = ""
        composeTestRule.setContent {
            BatteryTheme { ChargeAlarmCard(false, "", false, onToggle = {}, onLimitChange = { typed = it }) }
        }

        composeTestRule.onNode(hasSetTextAction()).performTextInput("85")

        assertEquals("85", typed)
    }
}
