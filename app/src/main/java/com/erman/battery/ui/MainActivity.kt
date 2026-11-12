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
import com.erman.battery.R
import com.erman.battery.data.BatteryStatus
import com.erman.battery.data.ChargingMode
import com.erman.battery.data.CurrentReading
import com.erman.battery.databinding.ActivityMainBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

//logo is from Smashicons flaticon.com
//battery discharge icon Icongeek26 flaticon.com
//usb charge icon Freepik flaticon.com
//ac charge icon Pixel perfect flaticon.com
//wireless charge icon Freepik flaticon.com
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpChargeAlarm(savedInstanceState)

        binding.infoButton.setOnClickListener { showBatteryInfo() }
        binding.usageButton.setOnClickListener { openBatteryUsage() }
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        subscriptions.addAll(
            viewModel.status.subscribe(::showStatus),
            viewModel.current.subscribe(::showCurrent),
            viewModel.isAlarmEnabled.subscribe { enabled ->
                binding.alarmSwitch.isChecked = enabled
                binding.alarmPercentageEditText.isEnabled = !enabled
            },
            viewModel.isLimitInvalid.subscribe { invalid ->
                binding.alarmPercentageEditText.error =
                    if (invalid) getString(R.string.charge_alarm_limit_error) else null
            }
        )
    }

    override fun onStop() {
        subscriptions.clear()
        super.onStop()
    }

    private fun setUpChargeAlarm(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.alarmPercentageEditText.setText(viewModel.savedAlarmLimit?.toString())
        }
        binding.alarmPercentageEditText.doAfterTextChanged { viewModel.onLimitChanged(it) }
        binding.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAlarmToggled(isChecked, binding.alarmPercentageEditText.text)
        }
    }

    private fun showBatteryInfo() {
        subscriptions.add(viewModel.status.firstElement().subscribe(::showBatteryInfo))
    }

    private fun showBatteryInfo(status: BatteryStatus) {
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

    private fun showCurrent(current: CurrentReading) {
        binding.chargeAmperTextView.text =
            if (current.microAmps != null) getString(R.string.current_value, current.microAmps)
            else getString(R.string.not_available)
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
