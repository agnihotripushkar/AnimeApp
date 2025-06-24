package com.devpush.animeapp.features.settings.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R

@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = listOf(
        stringResource(R.string.theme_option_light) to "light",
        stringResource(R.string.theme_option_dark) to "dark",
        stringResource(R.string.theme_option_system) to "system"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.theme_selection_title)) },
        text = {
            Column {
                themes.forEach { (themeName, themeValue) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onThemeSelected(themeValue)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == themeValue,
                            onClick = {
                                onThemeSelected(themeValue)
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(themeName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    )
}