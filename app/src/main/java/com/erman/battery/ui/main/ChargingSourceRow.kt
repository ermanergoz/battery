package com.erman.battery.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.data.ChargingMode
import com.erman.battery.ui.iconRes
import com.erman.battery.ui.sourceLabelRes
import com.erman.battery.ui.theme.BatteryTheme

@Composable
fun ChargingSourceRow(selected: ChargingMode, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ChargingMode.values().forEach { mode ->
            ChargingSourceChip(mode, isSelected = mode == selected, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ChargingSourceChip(mode: ChargingMode, isSelected: Boolean, modifier: Modifier) {
    val colors = BatteryTheme.extendedColors
    val background by animateColorAsState(
        if (isSelected) colors.selected else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )
    val content by animateColorAsState(
        if (isSelected) colors.onSelected else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 200)
    )
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .semantics(mergeDescendants = true) { selected = isSelected }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(mode.iconRes),
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(mode.sourceLabelRes),
            style = MaterialTheme.typography.caption,
            color = content,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun ChargingSourceRowPreview() {
    BatteryTheme { ChargingSourceRow(selected = ChargingMode.AC) }
}
