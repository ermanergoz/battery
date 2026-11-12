package com.erman.battery.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.R
import com.erman.battery.data.BatteryHealth
import com.erman.battery.ui.components.SectionCard
import com.erman.battery.ui.formatCurrent
import com.erman.battery.ui.formatTemperature
import com.erman.battery.ui.formatVoltage
import com.erman.battery.ui.labelRes
import com.erman.battery.ui.theme.BatteryTheme

@Composable
fun BatteryStats(
    current: Int?,
    voltage: Float,
    temperature: Float,
    health: BatteryHealth,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile(stringResource(R.string.current), context.formatCurrent(current), Modifier.weight(1f))
            StatTile(stringResource(R.string.voltage), context.formatVoltage(voltage), Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile(stringResource(R.string.battery_temp), context.formatTemperature(temperature), Modifier.weight(1f))
            StatTile(stringResource(R.string.health), stringResource(health.labelRes), Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, modifier: Modifier) {
    SectionCard(modifier.semantics(mergeDescendants = true) {}) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = label, style = MaterialTheme.typography.subtitle2)
        }
        Spacer(Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.h5, modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun BatteryStatsPreview() {
    BatteryTheme { BatteryStats(357421, 3989f, 26f, BatteryHealth.GOOD) }
}
