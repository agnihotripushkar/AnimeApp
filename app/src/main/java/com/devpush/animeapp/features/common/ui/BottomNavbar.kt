package com.devpush.animeapp.features.common.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TransitEnterexit
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devpush.animeapp.core.navigation.model.BottomNavItem
import com.devpush.animeapp.core.navigation.routes.NavRoute

/**
 * Bottom navigation items for the main app screens.
 */
private val bottomNavItems = listOf(
    BottomNavItem(
        route = NavRoute.Home.route,
        label = "Home",
        icon = Icons.Default.Home,
        contentDescription = "Home"
    ),
    BottomNavItem(
        route = NavRoute.Trending.route,
        label = "Trending",
        icon = Icons.Default.TransitEnterexit,
        contentDescription = "Trending"
    ),
    BottomNavItem(
        route = NavRoute.Settings.route,
        label = "Settings",
        icon = Icons.Default.Settings,
        contentDescription = "Settings"
    )
)

/**
 * Bottom navigation bar component that displays navigation items.
 * 
 * @param currentRoute The currently active route
 * @param onNavigate Callback invoked when a navigation item is clicked
 * @param modifier Optional modifier for the navigation bar
 */
@Composable
fun BottomNavbar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
