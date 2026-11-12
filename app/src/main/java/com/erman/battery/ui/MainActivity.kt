package com.erman.battery.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.erman.battery.R
import com.erman.battery.data.BatteryStatus
import com.erman.battery.data.ChargingMode
import com.erman.battery.databinding.ActivityMainBinding

//logo is from Smashicons flaticon.com
//battery discharge icon Icongeek26 flaticon.com
//usb charge icon Freepik flaticon.com
//ac charge icon Pixel perfect flaticon.com
//wireless charge icon Freepik flaticon.com
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.status.observe(this, Observer { showStatus(it) })
        viewModel.current.observe(this, Observer { current ->
            binding.chargeAmperTextView.text =
                if (current != null) getString(R.string.current_value, current)
                else getString(R.string.not_available)
        })

        setUpChargeAlarm(savedInstanceState)

        binding.infoButton.setOnClickListener { showBatteryInfo() }
        binding.usageButton.setOnClickListener { openBatteryUsage() }
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setUpChargeAlarm(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.alarmPercentageEditText.setText(viewModel.savedAlarmLimit?.toString())
        }
        binding.alarmPercentageEditText.doAfterTextChanged { viewModel.onLimitChanged(it) }
        binding.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAlarmToggled(isChecked, binding.alarmPercentageEditText.text)
        }

        viewModel.isAlarmEnabled.observe(this, Observer { enabled ->
            binding.alarmSwitch.isChecked = enabled
            binding.alarmPercentageEditText.isEnabled = !enabled
        })
        viewModel.isLimitInvalid.observe(this, Observer { invalid ->
            binding.alarmPercentageEditText.error =
                if (invalid) getString(R.string.charge_alarm_limit_error) else null
        })
    }

    private fun showBatteryInfo() {
        val status = viewModel.status.value ?: return
        AlertDialog.Builder(this)
            .setTitle(R.string.battery_info_title)
            .setMessage(
                getString(
                    R.string.battery_info_message,
                    getString(status.health.labelRes),
                    getString(status.chargeState.labelRes),
                    status.technology ?: getString(R.string.unknown)
                )
            )
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun openBatteryUsage() {
        try {
            startActivity(Intent(Intent.ACTION_POWER_USAGE_SUMMARY))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.battery_usage_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showStatus(status: BatteryStatus) {
        binding.batteryPercentageTextView.text = status.percentage.toString()
        binding.batteryTempTextView.text = getString(R.string.temperature_value, status.temperature)
        binding.voltageTextView.text = getString(R.string.voltage_value, status.voltage)
        updateChargeStatusIndicator(status.chargingMode)
    }

    private fun updateChargeStatusIndicator(chargingMode: ChargingMode) {
        resetChargeStatusIndicator()

        when (chargingMode) {
            ChargingMode.USB -> binding.usbChargeImageView.setBackgroundColor(Color.BLUE)
            ChargingMode.AC -> binding.acChargeImageView.setBackgroundColor(Color.GREEN)
            ChargingMode.WIRELESS -> binding.wirelessChargeImageView.setBackgroundColor(Color.YELLOW)
            ChargingMode.DISCHARGING -> binding.dechargeImageView.setBackgroundColor(Color.RED)
        }
    }

    private fun resetChargeStatusIndicator() {
        binding.usbChargeImageView.setBackgroundColor(Color.TRANSPARENT)
        binding.acChargeImageView.setBackgroundColor(Color.TRANSPARENT)
        binding.dechargeImageView.setBackgroundColor(Color.TRANSPARENT)
        binding.wirelessChargeImageView.setBackgroundColor(Color.TRANSPARENT)
    }
}
