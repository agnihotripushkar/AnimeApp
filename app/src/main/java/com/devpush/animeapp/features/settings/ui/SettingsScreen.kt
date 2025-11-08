package com.devpush.animeapp.features.settings.ui

import android.app.Activity
import androidx.biometric.BiometricManager
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R
import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthStatus
import com.devpush.animeapp.features.settings.ui.utils.LanguageSelectionDialog
import com.devpush.animeapp.features.settings.ui.utils.SettingsItem
import com.devpush.animeapp.features.settings.ui.utils.SettingsSwitchItem
import com.devpush.animeapp.features.settings.ui.utils.ThemeSelectionDialog
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(
            LocalContext.current as Activity
        )
    )

    val context = LocalContext.current
    val biometricManager = BiometricManager.from(context)
    val canAuthenticate =
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }

    var showThemeDialog by remember { mutableStateOf(false) }
    val currentTheme by settingsViewModel.appTheme.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    val currentLanguage by settingsViewModel.appLanguage.collectAsState()
    val isBiometricAuthEnabled by settingsViewModel.isBiometricAuthEnabled.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (canAuthenticate) {
            settingsViewModel.updateAuthStatus(BiometricAuthStatus.IDLE) // Reset status
        } else {
            settingsViewModel.updateAuthStatus(BiometricAuthStatus.NOT_AVAILABLE)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
        Box(modifier = Modifier.padding(paddingValues)) {
            if (windowSize == DevicePosture.EXPANDED_WIDTH) {
                SettingsExpanded(
                    scrollState = scrollState,
                    settingsViewModel = settingsViewModel,
                    isBiometricAuthEnabled = isBiometricAuthEnabled,
                    onLogout = onLogout,
                    currentTheme = currentTheme,
                    showThemeDialog = showThemeDialog,
                    onShowThemeDialogChange = { showThemeDialog = it },
                    currentLanguage = currentLanguage,
                    showLanguageDialog = showLanguageDialog,
                    onShowLanguageDialogChange = { showLanguageDialog = it }
                )
            } else {
                SettingsCompact(
                    scrollState = scrollState,
                    settingsViewModel = settingsViewModel,
                    isBiometricAuthEnabled = isBiometricAuthEnabled,
                    onLogout = onLogout,
                    currentTheme = currentTheme,
                    showThemeDialog = showThemeDialog,
                    onShowThemeDialogChange = { showThemeDialog = it },
                    currentLanguage = currentLanguage,
                    showLanguageDialog = showLanguageDialog,
                    onShowLanguageDialogChange = { showLanguageDialog = it }
                )
            }
        }
    }
}

@Composable
fun SettingsCompact(
    scrollState: ScrollState,
    settingsViewModel: SettingsViewModel,
    isBiometricAuthEnabled: Boolean,
    onLogout: () -> Unit,
    currentTheme: String,
    showThemeDialog: Boolean,
    onShowThemeDialogChange: (Boolean) -> Unit,
    currentLanguage: String,
    showLanguageDialog: Boolean,
    onShowLanguageDialogChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
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
            title = stringResource(R.string.settings_biometric_auth_toggle_title),
            checked = isBiometricAuthEnabled,
            onCheckedChange = { settingsViewModel.setBiometricAuthEnabled(it) }
        )
        SettingsItem(
            title = stringResource(R.string.logout_title),
            showArrow = true,
            onClick = {
                settingsViewModel.performLogout()
                onLogout()
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
            onClick = { onShowThemeDialogChange(true) }
        )
        SettingsItem(
            title = stringResource(R.string.app_language_title),
            subtitle = currentLanguage.let { langCode -> if (langCode == "es") "Español" else "English" },
            showArrow = true,
            onClick = { onShowLanguageDialogChange(true) }
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
            onDismiss = { onShowThemeDialogChange(false) }
        )
    }
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { lang ->
                settingsViewModel.setAppLanguage(lang)
            },
            onDismiss = { onShowLanguageDialogChange(false) }
        )
    }
}

@Composable
fun SettingsExpanded(
    scrollState: ScrollState,
    settingsViewModel: SettingsViewModel,
    isBiometricAuthEnabled: Boolean,
    onLogout: () -> Unit,
    currentTheme: String,
    showThemeDialog: Boolean,
    onShowThemeDialogChange: (Boolean) -> Unit,
    currentLanguage: String,
    showLanguageDialog: Boolean,
    onShowLanguageDialogChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
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
            title = stringResource(R.string.settings_biometric_auth_toggle_title),
            checked = isBiometricAuthEnabled,
            onCheckedChange = { settingsViewModel.setBiometricAuthEnabled(it) }
        )
        SettingsItem(
            title = stringResource(R.string.logout_title),
            showArrow = true,
            onClick = {
                settingsViewModel.performLogout()
                onLogout()
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
            onClick = { onShowThemeDialogChange(true) }
        )
        SettingsItem(
            title = stringResource(R.string.app_language_title),
            subtitle = currentLanguage.let { langCode -> if (langCode == "es") "Español" else "English" },
            showArrow = true,
            onClick = { onShowLanguageDialogChange(true) }
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
            onDismiss = { onShowThemeDialogChange(false) }
        )
    }
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { lang ->
                settingsViewModel.setAppLanguage(lang)
            },
            onDismiss = { onShowLanguageDialogChange(false) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AnimeAppTheme(
        userThemePreference = "Dark",
    ) {
        SettingsScreen(
            onNavigateBack = {},
            onLogout = {}
        )
    }
}