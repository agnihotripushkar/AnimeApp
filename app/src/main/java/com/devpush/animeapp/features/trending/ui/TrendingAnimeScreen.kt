package com.devpush.animeapp.features.trending.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.wear.compose.material3.TextButton
import androidx.wear.compose.material3.TextButtonDefaults
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.ui.utils.AnimeCard
import com.devpush.animeapp.features.trending.ui.utils.AnimePoster
import com.devpush.animeapp.features.trending.ui.utils.SegmentedToggleButton
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingAnimeScreen(
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onSettingsClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: TrendingAnimeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(true) }
    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
    // Not used
    val standardColors = FloatingToolbarDefaults.standardFloatingToolbarColors()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.animes)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    )
    { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(scaffoldPadding)
        ) {
            SegmentedToggleButton(
                selectedCategory = selectedCategory,
                onCategorySelected = { category -> viewModel.onCategorySelected(category) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            )
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
                            Box( // Box to contain LazyColumn and FloatingToolbar
                                Modifier
                                    .fillMaxSize()
                            ) {
                                if (selectedCategory == AnimeCategory.ALL) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(0.dp),
                                        contentPadding = PaddingValues(bottom = 80.dp) // Padding for FAB visibility
                                    )
                                    {
                                        items(
                                            animeDataList.size,
                                            key = { animeDataList[it].id }
                                        ) { index ->
                                            val dismissState = rememberSwipeToDismissBoxState(
                                                confirmValueChange = { direction ->
                                                    when (direction) {
                                                        SwipeToDismissBoxValue.EndToStart -> { // Swiped Left (Archive)
                                                            viewModel.archiveAnime(
                                                                animeDataList[index].id,
                                                                animeDataList[index].isArchived
                                                            )
                                                            false // Prevent immediate dismissal
                                                        }

                                                        SwipeToDismissBoxValue.StartToEnd -> { // Swiped Right (Star)
                                                            viewModel.starAnime(
                                                                animeDataList[index].id,
                                                                animeDataList[index].isFavorite
                                                            )
                                                            false // Prevent immediate dismissal
                                                        }

                                                        SwipeToDismissBoxValue.Settled -> false
                                                    }
                                                }
                                            )
                                            AnimeCard(
                                                anime = animeDataList[index],
                                                onClick = {
                                                    onAnimeClick(
                                                        animeDataList[index].attributes.posterImage.originalUrl,
                                                        animeDataList[index].id
                                                    )
                                                },
                                                onStar = {
                                                    viewModel.starAnime(
                                                        animeDataList[index].id,
                                                        animeDataList[index].isFavorite
                                                    )
                                                },
                                                onArchive = {
                                                    viewModel.archiveAnime(
                                                        animeDataList[index].id,
                                                        animeDataList[index].isArchived
                                                    )
                                                },
                                                dismissState = dismissState
                                            )
                                        }
                                    }
                                } else {
                                    LazyVerticalGrid(
                                        columns = GridCells.Adaptive(minSize = 180.dp), // Alternative: Adaptive columns
                                        contentPadding = PaddingValues(8.dp)
                                    )
                                    {
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
                                    modifier =
                                        Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(all = 16.dp),
                                    colors = vibrantColors,
                                    content = {
                                        IconButton(onClick = { onFavoriteClick() }) {
                                            Icon(
                                                Icons.Filled.Star,
                                                contentDescription = stringResource(R.string.favorite)
                                            )
                                        }
//                                    IconButton(onClick = { onBookmarkClick }) {
//                                        Icon(
//                                            Icons.Filled.Bookmark,
//                                            contentDescription = stringResource(R.string.bookmark)
//                                        )
//                                    }
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
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrendingAnimeScreenPreview() {
    AnimeAppTheme {
        TrendingAnimeScreen(
            onAnimeClick = { _, _ -> },
            onSettingsClick = {},
            onArchiveClick = {},
            onFavoriteClick = {}
        )
    }

}