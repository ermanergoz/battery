package com.erman.battery.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.R
import com.erman.battery.data.ChargeState
import com.erman.battery.ui.components.SectionCard
import com.erman.battery.ui.labelRes
import com.erman.battery.ui.theme.BatteryTheme
import com.erman.battery.ui.theme.PillShape

@Composable
fun BatteryDetailsCard(
    chargeState: ChargeState,
    technology: String?,
    onUsageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier) {
        Text(text = stringResource(R.string.details), style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))
        DetailRow(stringResource(R.string.status), stringResource(chargeState.labelRes))
        DetailRow(stringResource(R.string.technology), technology ?: stringResource(R.string.unknown))
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onUsageClick, shape = PillShape, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.battery_usage))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .semantics(mergeDescendants = true) {}
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = label, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))
        }
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
private fun BatteryDetailsCardPreview() {
    BatteryTheme { BatteryDetailsCard(ChargeState.CHARGING, "Li-ion", onUsageClick = {}) }
}
