package com.erman.battery.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.erman.battery.R
import com.erman.battery.ui.main.MainScreen
import com.erman.battery.ui.theme.BatteryTheme

//logo is from Smashicons flaticon.com
//battery discharge icon Icongeek26 flaticon.com
//usb charge icon Freepik flaticon.com
//ac charge icon Pixel perfect flaticon.com
//wireless charge icon Freepik flaticon.com
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatteryTheme {
                val status by viewModel.status.observeAsState()
                val current by viewModel.current.observeAsState()
                val isAlarmEnabled by viewModel.isAlarmEnabled.observeAsState(false)
                val limitText by viewModel.limitText.observeAsState("")
                val isLimitInvalid by viewModel.isLimitInvalid.observeAsState(false)

                MainScreen(
                    status = status,
                    current = current,
                    isAlarmEnabled = isAlarmEnabled,
                    limitText = limitText,
                    isLimitInvalid = isLimitInvalid,
                    onAlarmToggled = viewModel::onAlarmToggled,
                    onLimitChanged = viewModel::onLimitChanged,
                    onUsageClick = ::openBatteryUsage,
                    onSettingsClick = ::openSettings
                )
            }
        }
    }

    private fun openBatteryUsage() {
        try {
            startActivity(Intent(Intent.ACTION_POWER_USAGE_SUMMARY))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.battery_usage_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}
