package com.erman.battery.ui.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.R
import com.erman.battery.data.ChargingMode
import com.erman.battery.ui.iconRes
import com.erman.battery.ui.labelRes
import com.erman.battery.ui.theme.BatteryTheme

private const val LOW_BATTERY_PERCENTAGE = 15

@Composable
fun BatteryGauge(percentage: Int, chargingMode: ChargingMode, modifier: Modifier = Modifier) {
    val progress by animateFloatAsState(
        targetValue = percentage.coerceIn(0, 100) / 100f,
        animationSpec = tween(durationMillis = 300)
    )
    val isLow = percentage <= LOW_BATTERY_PERCENTAGE && chargingMode == ChargingMode.DISCHARGING
    val ringColor = if (isLow) MaterialTheme.colors.error else BatteryTheme.extendedColors.gauge
    val trackColor = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    val modeLabel = stringResource(chargingMode.labelRes)
    val description = stringResource(R.string.gauge_description, percentage, modeLabel)

    Box(
        modifier = modifier
            .size(200.dp)
            .semantics(mergeDescendants = true) { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            drawArc(trackColor, 0f, 360f, false, topLeft, arcSize, style = Stroke(strokeWidth))
            drawArc(
                ringColor, -90f, 360f * progress, false, topLeft, arcSize,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.percentage_value, percentage),
                style = MaterialTheme.typography.h3
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(chargingMode.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(text = modeLabel, style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview
@Composable
private fun BatteryGaugePreview() {
    BatteryTheme { BatteryGauge(percentage = 61, chargingMode = ChargingMode.USB) }
}
