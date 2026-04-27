package com.devpush.animeapp.features.common.ui

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devpush.animeapp.core.navigation.routes.NavRoute

/**
 * Side navigation rail for expanded-width (tablet/landscape) layouts.
 *
 * Mirrors the interface of [BottomNavbar] and reuses [bottomNavItems] so
 * destination definitions are not duplicated.
 *
 * @param currentRoute The currently active route — used to highlight the selected item.
 * @param onHomeClick     Callback invoked when the Home destination is clicked.
 * @param onTrendingClick Callback invoked when the Trending destination is clicked.
 * @param onSettingsClick Callback invoked when the Settings destination is clicked.
 * @param modifier        Optional modifier for the rail.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WideNavigationRailBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onTrendingClick: () -> Unit,
    onSettingsClick: () -> Unit,
    railExpanded: Boolean = false,
    modifier: Modifier = Modifier
) {
    WideNavigationRail(modifier = modifier) {
        bottomNavItems.forEach { item ->
            WideNavigationRailItem(
                selected = currentRoute == item.route,
                onClick = {
                    when (item.route) {
                        NavRoute.Home.route -> onHomeClick()
                        NavRoute.Trending.route -> onTrendingClick()
                        NavRoute.Settings.route -> onSettingsClick()
                    }
                },
                railExpanded = railExpanded,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
