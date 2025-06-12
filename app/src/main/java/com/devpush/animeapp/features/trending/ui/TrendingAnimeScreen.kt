package com.devpush.animeapp.features.trending.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.ui.utils.AnimeCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingAnimeScreen(
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onSettingsClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    viewModel: TrendingAnimeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(true) }
    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
    // Not used
    val standardColors = FloatingToolbarDefaults.standardFloatingToolbarColors()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.trending_anime)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        // Use AnimatedContent to switch between Loading, Success, and Error states
        AnimatedContent(
            targetState = uiState, // Animate based on the entire uiState object
            label = "TrendingAnimeContent"
            // You can add transitionSpec here for custom animations if desired
        ) { targetState ->
            when (targetState) {
                TrendingAnimeUiState.Loading, TrendingAnimeUiState.Idle -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        ContainedLoadingIndicator()
                    }
                }

                is TrendingAnimeUiState.Success -> {
                    val animeDataList = targetState.animeList
                    if (animeDataList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_trending_anime_found))
                        }
                    } else {
                        Box( // Box to contain LazyColumn and FloatingToolbar
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                items(
                                    animeDataList.size
                                ) { index ->
                                    AnimeCard(
                                        anime = animeDataList[index],
                                        onClick = {
                                            onAnimeClick(
                                                animeDataList[index].attributes.posterImage.original,
                                                animeDataList[index].id
                                            )
                                        },
                                        onStar = { viewModel.starAnime(animeDataList[index].id) },
                                        onArchive = { viewModel.archiveAnime(animeDataList[index].id) }
                                    )
                                }
                            }
                            HorizontalFloatingToolbar(
                                expanded = expanded,
                                floatingActionButton = {
                                    FloatingToolbarDefaults.VibrantFloatingActionButton(
                                        onClick = { onArchiveClick() },
                                    ) {
                                        Icon(Icons.Filled.Archive, contentDescription = "")
                                    }
                                },
                                modifier =
                                    Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(all = 16.dp),
                                colors = vibrantColors,
                                content = {
                                    IconButton(onClick = { onFavoriteClick() }) {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = stringResource(R.string.favorite)
                                        )
                                    }
                                    IconButton(onClick = { onBookmarkClick() }) {
                                        Icon(
                                            Icons.Filled.Bookmark,
                                            contentDescription = stringResource(R.string.bookmark)
                                        )
                                    }
                                    IconButton(onClick = { onSettingsClick() }) {
                                        Icon(
                                            Icons.Filled.Settings,
                                            contentDescription = stringResource(R.string.settings)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                is TrendingAnimeUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = targetState.message
                                    ?: stringResource(R.string.an_error_occurred),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.retryFetchTrendingAnime() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }
    }
}