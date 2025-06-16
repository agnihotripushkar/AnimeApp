package com.devpush.animeapp.features.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devpush.animeapp.R
import com.devpush.animeapp.core.navigation.NavGraph
import com.devpush.animeapp.features.auth.ui.login.LoginScreen
import com.devpush.animeapp.features.settings.ui.utils.SettingsItem
import com.devpush.animeapp.features.settings.ui.utils.SettingsSwitchItem
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.devpush.animeapp.features.settings.ui.SettingsViewModel
import com.devpush.animeapp.features.settings.ui.utils.LanguageSelectionDialog
import com.devpush.animeapp.features.settings.ui.utils.ThemeSelectionDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {

    var showThemeDialog by remember { mutableStateOf(false) }
    val currentTheme by settingsViewModel.appTheme.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    val currentLanguage by settingsViewModel.appLanguage.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(all = 16.dp)
        ) {
            // Account Settings Section
            Text(
                text = stringResource(R.string.account_settings_section_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SettingsItem(
                title = stringResource(R.string.profile_info_title),
                subtitle = stringResource(R.string.profile_info_subtitle),
                showArrow = true
            )
            SettingsItem(title = stringResource(R.string.change_password_title), showArrow = true)
            SettingsSwitchItem(
                title = stringResource(R.string.biometric_login_title),
                initialChecked = false
            )
            SettingsItem(
                title = stringResource(R.string.logout_title),
                showArrow = true,
                onClick = {
                    settingsViewModel.performLogout()
                    navController.navigate(NavGraph.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Theme Section
            Text(
                text = stringResource(R.string.app_theme_section_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SettingsItem(
                title = stringResource(R.string.theme_selection_title),
                subtitle = currentTheme.let { theme -> theme.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } + if (theme == "system") " (Default)" else "" },
                showArrow = true,
                onClick = { showThemeDialog = true }
            )
            SettingsItem(
                title = stringResource(R.string.app_language_title),
                subtitle = currentLanguage.let { langCode -> if (langCode == "es") "Español" else "English" },
                showArrow = true,
                onClick = { showLanguageDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Info Section
            Text(
                text = stringResource(R.string.app_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SettingsItem(title = stringResource(R.string.app_version_title), subtitle = "1.0.0")
            SettingsItem(title = stringResource(R.string.terms_conditions_title), showArrow = true)
            SettingsItem(title = stringResource(R.string.privacy_policy_title), showArrow = true)
        }

        if (showThemeDialog) {
            ThemeSelectionDialog(
                currentTheme = currentTheme,
                onThemeSelected = { theme ->
                    settingsViewModel.setAppTheme(theme)
                },
                onDismiss = { showThemeDialog = false }
            )
        }
        if (showLanguageDialog) {
            LanguageSelectionDialog(
                currentLanguage = currentLanguage,
                onLanguageSelected = { lang ->
                    settingsViewModel.setAppLanguage(lang)
                },
                onDismiss = { showLanguageDialog = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AnimeAppTheme(
        userThemePreference = "Dark",
    ) {
        SettingsScreen(rememberNavController())
    }
}