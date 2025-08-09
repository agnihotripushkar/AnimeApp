package com.devpush.animeapp.features.archived.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devpush.animeapp.R
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.trending.ui.utils.AnimeCard
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun ArchivedAnimeScreen(
    navController: NavController,
    onAnimeClick: (animeId: String) -> Unit = { Timber.d("Anime card clicked: $it") },
    viewModel: ArchivedAnimeViewModel = koinViewModel()
) {
    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(
            LocalContext.current as Activity
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.archived_anime)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (windowSize == DevicePosture.EXPANDED_WIDTH) {
                ArchivedAnimeExpanded(
                    modifier = Modifier.padding(innerPadding),
                    onAnimeClick = onAnimeClick,
                    viewModel = viewModel,
                    uiState = uiState
                )
            } else {
                ArchivedAnimeCompact(
                    modifier = Modifier.padding(innerPadding),
                    onAnimeClick = onAnimeClick,
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArchivedAnimeCompact(
    modifier: Modifier,
    onAnimeClick: (animeId: String) -> Unit,
    viewModel: ArchivedAnimeViewModel,
    uiState: ArchivedAnimeUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            ContainedLoadingIndicator()
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else if (uiState.animes.isEmpty()) {
            Text(stringResource(R.string.no_archived_animes_yet))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.animes,
                    key = { anime -> anime.id }
                ) { anime: AnimeDataEntity ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { direction ->
                            when (direction) {
                                SwipeToDismissBoxValue.EndToStart -> { // Swiped Left (Archive)
                                    viewModel.archiveAnime(
                                        anime.id,
                                        anime.isArchived
                                    )
                                    false // Prevent immediate dismissal
                                }

                                SwipeToDismissBoxValue.StartToEnd -> false

                                SwipeToDismissBoxValue.Settled -> false
                            }
                        }
                    )
                    AnimeCard(
                        anime = anime,
                        onClick = { onAnimeClick(anime.id) },
                        onStar = {
                            // Star action not implemented for this screen
                            Timber.d("Star clicked for ${anime.id} on Archived screen")

                        },
                        onArchive = {
                            viewModel.toggleArchivedStatus(anime.id, anime.isArchived)

                        },
                        dismissState = dismissState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArchivedAnimeExpanded(
    modifier: Modifier,
    onAnimeClick: (animeId: String) -> Unit,
    viewModel: ArchivedAnimeViewModel,
    uiState: ArchivedAnimeUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            ContainedLoadingIndicator()
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else if (uiState.animes.isEmpty()) {
            Text(stringResource(R.string.no_archived_animes_yet))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.animes,
                    key = { anime -> anime.id }
                ) { anime: AnimeDataEntity ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { direction ->
                            when (direction) {
                                SwipeToDismissBoxValue.EndToStart -> { // Swiped Left (Archive)
                                    viewModel.archiveAnime(
                                        anime.id,
                                        anime.isArchived
                                    )
                                    false // Prevent immediate dismissal
                                }

                                SwipeToDismissBoxValue.StartToEnd -> false

                                SwipeToDismissBoxValue.Settled -> false
                            }
                        }
                    )
                    AnimeCard(
                        anime = anime,
                        onClick = { onAnimeClick(anime.id) },
                        onStar = {
                            // Star action not implemented for this screen
                            Timber.d("Star clicked for ${anime.id} on Archived screen")

                        },
                        onArchive = {
                            viewModel.toggleArchivedStatus(anime.id, anime.isArchived)

                        },
                        dismissState = dismissState
                    )
                }
            }
        }
    }
}