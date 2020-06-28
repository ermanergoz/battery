package com.erman.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle

class BroadcastReceiver() : BroadcastReceiver() {

    private lateinit var listener: BroadcastReceiverListener

    override fun onReceive(context: Context?, intent: Intent?) {

        try {
            listener = context as BroadcastReceiverListener
        } catch (err: ClassCastException) {
            throw ClassCastException(("$context must implement BroadcastReceiverListener"))
        }

        val batteryPercentage = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)?.toFloat()
        val batteryTemp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)?.toFloat()?.div(10)
        val chargingStatus = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

        val bundle = Bundle()

        batteryPercentage?.let { bundle.putInt(KEY_BUNDLE_BATTERY_PERCENTAGE, it) }
        voltage?.let { bundle.putFloat(KEY_BUNDLE_BATTERY_VOLTAGE, it) }
        batteryTemp?.let { bundle.putFloat(KEY_BUNDLE_BATTERY_TEMP, it) }
        chargingStatus?.let { bundle.putInt(KEY_BUNDLE_CHARGING_STATUS, it) }

        listener.broadcastReceiverListener(bundle)
    }

    interface BroadcastReceiverListener {
        fun broadcastReceiverListener(bundle: Bundle)
    }

}