package com.devpush.animeapp.features.trending.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.ui.utils.AnimePoster
import com.devpush.animeapp.features.trending.utils.FabPositioning

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingAnimeExpanded(
    uiState: TrendingAnimeUiState,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    expanded: Boolean,
    onSettingsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    vibrantColors: androidx.compose.material3.FloatingToolbarColors,
    onRetry: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
    ) {
        // Use AnimatedContent to switch between Loading, Success, and Error states
        // Temporarily replaced AnimatedContent with a direct when statement for diagnosis
        Box(modifier = Modifier.weight(1f)) { // Added a Box to maintain similar layout structure as AnimatedContent
            when (val currentUiState = uiState) { // Used a val for smart casting
                TrendingAnimeUiState.Loading, TrendingAnimeUiState.Idle -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ContainedLoadingIndicator()
                    }
                }

                is TrendingAnimeUiState.Success -> {
                    val animeDataList = currentUiState.animeList
                    if (animeDataList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_trending_anime_found))
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyVerticalGrid(
                                modifier = Modifier.fillMaxSize(),
                                columns = GridCells.Adaptive(minSize = 180.dp),
                                contentPadding = FabPositioning.GRID_CONTENT_PADDING
                            ) {
                                items(
                                    animeDataList.size,
                                    key = { animeDataList[it].id }
                                ) { index ->
                                    AnimePoster(
                                        anime = animeDataList[index],
                                        onClick = {
                                            onAnimeClick(
                                                animeDataList[index].attributes.posterImage.originalUrl,
                                                animeDataList[index].id
                                            )
                                        },
                                    )
                                }
                            }
                        }

                        HorizontalFloatingToolbar(
                            expanded = expanded,
                            floatingActionButton = {
                                FloatingToolbarDefaults.VibrantFloatingActionButton(
                                    onClick = { onSettingsClick() },
                                ) {
                                    Icon(
                                        Icons.Filled.Settings,
                                        contentDescription = stringResource(R.string.settings)
                                    )
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            colors = vibrantColors,
                            content = {
                                IconButton(onClick = { onFavoriteClick() }) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = stringResource(R.string.favorite)
                                    )
                                }
                                IconButton(onClick = { onArchiveClick() }) {
                                    Icon(
                                        Icons.Filled.Archive,
                                        contentDescription = stringResource(R.string.archive)
                                    )
                                }
                            }
                        )
                    }
                }

                is TrendingAnimeUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = currentUiState.message // Use currentUiState for smart cast
                                    ?: stringResource(R.string.an_error_occurred),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onRetry() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }
    }
}