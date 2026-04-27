package com.devpush.animeapp.features.trending.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devpush.animeapp.R
import com.devpush.animeapp.core.navigation.routes.NavRoute
import com.devpush.animeapp.core.presentation.ObserveAsEvents
import com.devpush.animeapp.features.common.ui.BottomNavbar
import com.devpush.animeapp.features.common.ui.WideNavigationRailBar
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun TrendingAnimeRoot(
    currentRoute: String?,
    onNavigateToHome: () -> Unit,
    onNavigateToTrending: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onArchiveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: TrendingAnimeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { /* No events to handle currently */ }

    TrendingAnimeScreen(
        currentRoute = currentRoute,
        state = state,
        onAction = viewModel::onAction,
        onNavigateToHome = onNavigateToHome,
        onNavigateToTrending = onNavigateToTrending,
        onNavigateToSettings = onNavigateToSettings,
        onAnimeClick = onAnimeClick,
        onArchiveClick = onArchiveClick,
        onFavoriteClick = onFavoriteClick
    )
}

@SuppressLint("ContextCastToActivity")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun TrendingAnimeScreen(
    currentRoute: String?,
    state: TrendingAnimeState,
    onAction: (TrendingAnimeAction) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToTrending: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onArchiveClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(LocalContext.current as Activity)
    )
    var expanded by rememberSaveable { mutableStateOf(true) }
    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()

    if (windowSize == DevicePosture.EXPANDED_WIDTH) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.animes),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { scaffoldPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                WideNavigationRailBar(
                    currentRoute = currentRoute ?: NavRoute.Trending.route,
                    onHomeClick = onNavigateToHome,
                    onTrendingClick = onNavigateToTrending,
                    onSettingsClick = onNavigateToSettings
                )
                TrendingAnimeExpanded(
                    state = state,
                    onAction = onAction,
                    onAnimeClick = onAnimeClick,
                    expanded = expanded,
                    onSettingsClick = onNavigateToSettings,
                    vibrantColors = vibrantColors,
                    onFavoriteClick = onFavoriteClick,
                    onArchiveClick = onArchiveClick
                )
            }
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.animes),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                BottomNavbar(
                    currentRoute = currentRoute ?: NavRoute.Trending.route,
                    onHomeClick = onNavigateToHome,
                    onTrendingClick = onNavigateToTrending,
                    onSettingsClick = onNavigateToSettings
                )
            }
        ) { scaffoldPadding ->
            TrendingAnimeCompact(
                state = state,
                onAction = onAction,
                onAnimeClick = onAnimeClick,
                expanded = expanded,
                onExpandedChange = { expanded = it },
                onFavoriteClick = onFavoriteClick,
                onArchiveClick = onArchiveClick,
                vibrantColors = vibrantColors,
                scaffoldPadding = scaffoldPadding
            )
        }
    }
}
