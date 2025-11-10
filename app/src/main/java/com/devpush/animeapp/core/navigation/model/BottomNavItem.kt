package com.devpush.animeapp.core.navigation.model

import androidx.compose.ui.graphics.vector.ImageVector
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
)
