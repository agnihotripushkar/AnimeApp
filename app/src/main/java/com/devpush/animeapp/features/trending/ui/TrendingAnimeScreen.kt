package com.devpush.animeapp.features.trending.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.ui.utils.AnimeCard
import com.devpush.animeapp.features.trending.ui.utils.AnimePoster
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import org.koin.androidx.compose.koinViewModel
import android.app.Activity
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devpush.animeapp.core.navigation.routes.NavRoute
import com.devpush.animeapp.features.common.ui.BottomNavbar
import com.devpush.animeapp.features.trending.ui.utils.FabMenu
import com.devpush.animeapp.features.trending.utils.Constants
import com.devpush.animeapp.features.trending.utils.FabPositioning
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture
import timber.log.Timber

@SuppressLint("ContextCastToActivity")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun TrendingAnimeScreen(
    currentRoute: String?,
    onNavigateToHome: () -> Unit,
    onNavigateToTrending: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onArchiveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: TrendingAnimeViewModel = koinViewModel()
) {
    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(
            LocalContext.current as Activity
        )
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(true) }
    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()

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
                ),
//                navigationIcon = {
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Localized description",
//                            tint = MaterialTheme.colorScheme.onPrimary
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Localized description",
//                            tint = MaterialTheme.colorScheme.onPrimary
//                        )
//                    }
//                },
            )
        },
        floatingActionButton = {
            FabMenu(
                onClick = { fabItem ->
                    when (fabItem.text) {
                        Constants.FabMenuFav -> {
                            onFavoriteClick()
                        }

                        Constants.FabMenuArchive -> {
                            onArchiveClick()
                        }
                    }
                }
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
    )
    { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            if (windowSize == DevicePosture.EXPANDED_WIDTH) {
                TrendingAnimeExpanded(
                    uiState = uiState,
                    onAnimeClick = onAnimeClick,
                    expanded = expanded,
                    onSettingsClick = onNavigateToSettings,
                    vibrantColors = vibrantColors,
                    onRetry = { viewModel.retryFetchTrendingAnime() },
                    onFavoriteClick = onFavoriteClick,
                    onArchiveClick = onArchiveClick,
                )
            } else {
                TrendingAnimeCompact(
                    uiState = uiState,
                    onAnimeClick = onAnimeClick,
                    onRetry = { viewModel.retryFetchTrendingAnime() }
                )
            }
        }
    }
}