package com.erman.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
//logo is from Smashicons flaticon.com
class MainActivity : AppCompatActivity(), com.erman.battery.BroadcastReceiver.BroadcastReceiverListener {
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        broadcastReceiver = com.erman.battery.BroadcastReceiver()

        val listen: MutableLiveData<Int> = MutableLiveData<Int>()
        listen.value = getCurrent()
        listen.observe(this, Observer {
            listen.postValue(getCurrent())
            chargeAmperTextView.text = it.toString() + " uA"
        })
    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(broadcastReceiver)
    }

    private fun getCurrent(): Int {
        val manager = this.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) * -1
    }

    override fun broadcastReceiverListener(bundle: Bundle) {
        batteryPercentageTextView.text = bundle.getInt(KEY_BUNDLE_BATTERY_PERCENTAGE, 0).toString()
        batteryTempTextView.text = bundle.getFloat(KEY_BUNDLE_BATTERY_TEMP, 0f).toString() + " " + 0x00B0.toChar() + "C"
        voltageTextView.text = bundle.getFloat(KEY_BUNDLE_BATTERY_VOLTAGE, 0f).toString() + " mV"
    }
}
