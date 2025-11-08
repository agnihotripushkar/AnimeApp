package com.devpush.animeapp.core.navigation.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a bottom navigation item.
 * 
 * @param route The navigation route for this item
 * @param label The display label for this item
 * @param icon The icon to display for this item
 * @param contentDescription Accessibility description for the icon
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
)
