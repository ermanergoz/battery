package com.erman.battery.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.R
import com.erman.battery.data.BatteryStatus
import com.erman.battery.ui.components.SectionCard
import com.erman.battery.ui.theme.BatteryTheme

@Composable
fun MainScreen(
    status: BatteryStatus?,
    current: Int?,
    isAlarmEnabled: Boolean,
    limitText: String,
    isLimitInvalid: Boolean,
    onAlarmToggled: (Boolean) -> Unit,
    onLimitChanged: (String) -> Unit,
    onUsageClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Outlined.Settings, contentDescription = stringResource(R.string.settings))
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        }
    ) { padding ->
        if (status == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionCard {
                BatteryGauge(status.percentage, status.chargingMode, Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(20.dp))
                ChargingSourceRow(status.chargingMode)
            }
            BatteryStats(current, status.voltage, status.temperature, status.health)
            ChargeAlarmCard(isAlarmEnabled, limitText, isLimitInvalid, onAlarmToggled, onLimitChanged)
            BatteryDetailsCard(status.chargeState, status.technology, onUsageClick)
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    BatteryTheme {
        MainScreen(
            status = BatteryStatus.from(61, 100, 3989, 260, 2, 2, 2, "Li-ion"),
            current = 357421,
            isAlarmEnabled = false,
            limitText = "80",
            isLimitInvalid = false,
            onAlarmToggled = {},
            onLimitChanged = {},
            onUsageClick = {},
            onSettingsClick = {}
        )
    }
}
