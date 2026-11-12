package com.erman.battery.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.erman.battery.R
import com.erman.battery.data.BatteryStatus
import com.erman.battery.ui.MainActivity
import com.erman.battery.ui.formatCurrent
import com.erman.battery.ui.formatTemperature
import com.erman.battery.ui.formatVoltage
import com.erman.battery.ui.iconRes
import com.erman.battery.ui.labelRes

class BatteryNotifications(private val context: Context) {

    private val manager = NotificationManagerCompat.from(context)

    init {
        createChannels()
    }

    fun ongoing(status: BatteryStatus?, current: Int?, alarmLimit: Int?): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_STATUS)
            .setSmallIcon(status?.chargingMode?.iconRes ?: R.drawable.ic_decharge)
            .setContentIntent(openAppIntent())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_STATUS)

        if (alarmLimit != null) {
            builder.setSubText(context.getString(R.string.notification_alarm_subtext, alarmLimit))
        }

        if (status == null) {
            builder.setContentTitle(context.getString(R.string.app_name))
        } else {
            builder.setContentTitle(
                context.getString(
                    R.string.notification_status_title,
                    status.percentage,
                    context.getString(status.chargingMode.labelRes)
                )
            ).setContentText(
                context.getString(
                    R.string.notification_status_text,
                    context.formatTemperature(status.temperature),
                    context.formatVoltage(status.voltage),
                    context.formatCurrent(current)
                )
            )
        }
        return builder.build()
    }

    fun showOngoing(status: BatteryStatus, current: Int?, alarmLimit: Int?) {
        manager.notify(ONGOING_NOTIFICATION_ID, ongoing(status, current, alarmLimit))
    }

    fun showChargeAlarm(percentage: Int) {
        val text = context.getString(R.string.notification_alarm_text, percentage)
        val notification = NotificationCompat.Builder(context, CHANNEL_ALARM)
            .setSmallIcon(R.drawable.ic_ac_charger)
            .setContentTitle(context.getString(R.string.notification_alarm_title))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(openAppIntent())
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
        manager.notify(ALARM_NOTIFICATION_ID, notification)
    }

    private fun openAppIntent(): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val status = NotificationChannel(
            CHANNEL_STATUS,
            context.getString(R.string.channel_status_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.channel_status_description)
            setShowBadge(false)
        }
        val alarm = NotificationChannel(
            CHANNEL_ALARM,
            context.getString(R.string.channel_alarm_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.channel_alarm_description)
            enableVibration(true)
        }
        context.getSystemService(NotificationManager::class.java)
            ?.createNotificationChannels(listOf(status, alarm))
    }

    companion object {
        const val ONGOING_NOTIFICATION_ID = 1
        private const val ALARM_NOTIFICATION_ID = 2
        private const val CHANNEL_STATUS = "battery_status"
        private const val CHANNEL_ALARM = "charge_alarm"
    }
}
