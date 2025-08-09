package com.devpush.animeapp.features.settings.ui.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.ui.theme.AnimeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(title: String, subtitle: String? = null, showArrow: Boolean = false, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                if (subtitle != null) {
                    Text(text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (showArrow) {
                Icon(Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


@Preview(showBackground = true, name = "Settings Item with Subtitle and Arrow")
@Composable
fun SettingsItemPreview_WithSubtitleAndArrow() {
    AnimeAppTheme { // Wrap with your app's theme for correct styling
        SettingsItem(
            title = "Appearance",
            subtitle = "Customize theme and display options",
            showArrow = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Settings Item without Subtitle")
@Composable
fun SettingsItemPreview_NoSubtitle() {
    AnimeAppTheme {
        SettingsItem(
            title = "Notifications",
            showArrow = true,
            onClick = {}
        )
    }
}

