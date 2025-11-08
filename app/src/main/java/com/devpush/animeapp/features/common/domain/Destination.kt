package com.devpush.animeapp.features.common.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TransitEnterexit
import androidx.compose.ui.graphics.vector.ImageVector
import com.devpush.animeapp.utils.Constants.HOME_ANIME_SCREEN
import com.devpush.animeapp.utils.Constants.SETTINGS_SCREEN
import com.devpush.animeapp.utils.Constants.TRENDING_ANIME_SCREEN

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    HOME(HOME_ANIME_SCREEN, "Home", Icons.Default.Home, "Home"),
    TRENDING(TRENDING_ANIME_SCREEN, "Trending", Icons.Default.TransitEnterexit, "Trending"),
    SETTINGS(SETTINGS_SCREEN, "Settings", Icons.Default.Settings, "Settings")
}