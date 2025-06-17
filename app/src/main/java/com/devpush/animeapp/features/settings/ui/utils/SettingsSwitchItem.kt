package com.devpush.animeapp.features.settings.ui.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSwitchItem(
    title: String,
    checked: Boolean, // Parameter name changed from initialChecked to checked
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            // Toggle based on the provided 'checked' state
            onCheckedChange(!checked)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Switch(
                checked = checked, // Directly use the 'checked' parameter
                onCheckedChange = onCheckedChange // Pass the callback directly
            )
        }
    }
}