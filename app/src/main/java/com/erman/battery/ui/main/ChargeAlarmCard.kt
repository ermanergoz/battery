package com.erman.battery.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.R
import com.erman.battery.ui.components.SectionCard
import com.erman.battery.ui.theme.BatteryTheme

@Composable
fun ChargeAlarmCard(
    isEnabled: Boolean,
    limitText: String,
    isLimitInvalid: Boolean,
    onToggle: (Boolean) -> Unit,
    onLimitChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    SectionCard(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(value = isEnabled, role = Role.Switch, onValueChange = onToggle)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = stringResource(R.string.charge_alarm), style = MaterialTheme.typography.h6)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = stringResource(R.string.charge_alarm_summary), style = MaterialTheme.typography.body2)
                }
            }
            Spacer(Modifier.width(16.dp))
            Switch(checked = isEnabled, onCheckedChange = null)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = limitText,
            onValueChange = onLimitChange,
            label = { Text(stringResource(R.string.charge_limit_label)) },
            enabled = !isEnabled,
            isError = isLimitInvalid,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )
        val supportingText = when {
            isLimitInvalid -> stringResource(R.string.charge_alarm_limit_error)
            isEnabled -> stringResource(R.string.charge_limit_locked)
            else -> stringResource(R.string.charge_limit_helper)
        }
        Text(
            text = supportingText,
            style = MaterialTheme.typography.caption,
            color = if (isLimitInvalid) MaterialTheme.colors.error
            else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@Preview
@Composable
private fun ChargeAlarmCardPreview() {
    BatteryTheme { ChargeAlarmCard(false, "80", false, onToggle = {}, onLimitChange = {}) }
}
