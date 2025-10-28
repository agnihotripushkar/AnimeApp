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
import com.devpush.animeapp.features.trending.ui.utils.SegmentedToggleButton
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import org.koin.androidx.compose.koinViewModel
import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.text
import com.devpush.animeapp.features.trending.ui.utils.FabMenu
import com.devpush.animeapp.features.trending.utils.Constants
import com.devpush.animeapp.features.trending.utils.FabPositioning
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture

@SuppressLint("ContextCastToActivity")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun TrendingAnimeScreen(
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onSettingsClick: () -> Unit,
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

                        Constants.FabMenuSettings -> {
                            onSettingsClick()
                        }
                    }
                }
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
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category -> viewModel.onCategorySelected(category) },
                    uiState = uiState,
                    onAnimeClick = onAnimeClick,
                    onStar = { id, isFavorite -> viewModel.starAnime(id, isFavorite) },
                    onArchive = { id, isArchived -> viewModel.archiveAnime(id, isArchived) },
                    expanded = expanded,
                    onSettingsClick = onSettingsClick,
                    onFavoriteClick = onFavoriteClick,
                    onArchiveClick = onArchiveClick,
                    vibrantColors = vibrantColors,
                    onRetry = { viewModel.retryFetchTrendingAnime() }
                )
            } else {
                TrendingAnimeCompact(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category -> viewModel.onCategorySelected(category) },
                    uiState = uiState,
                    onAnimeClick = onAnimeClick,
                    onStar = { id, isFavorite -> viewModel.starAnime(id, isFavorite) },
                    onArchive = { id, isArchived -> viewModel.archiveAnime(id, isArchived) },
                    expanded = expanded,
                    onSettingsClick = onSettingsClick,
                    onFavoriteClick = onFavoriteClick,
                    onArchiveClick = onArchiveClick,
                    vibrantColors = vibrantColors,
                    onRetry = { viewModel.retryFetchTrendingAnime() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingAnimeCompact(
    selectedCategory: AnimeCategory,
    onCategorySelected: (AnimeCategory) -> Unit,
    uiState: TrendingAnimeUiState,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onStar: (id: String, isFavorite: Boolean) -> Unit,
    onArchive: (id: String, isArchived: Boolean) -> Unit,
    expanded: Boolean,
    onSettingsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    vibrantColors: androidx.compose.material3.FloatingToolbarColors,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
    ) {
        SegmentedToggleButton(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
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
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (selectedCategory == AnimeCategory.ALL) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(0.dp),
                                    contentPadding = FabPositioning.LIST_CONTENT_PADDING
                                ) {
                                    items(
                                        animeDataList.size,
                                        key = { animeDataList[it].id }
                                    ) { index ->
                                        val dismissState = rememberSwipeToDismissBoxState(
                                            confirmValueChange = { direction ->
                                                when (direction) {
                                                    SwipeToDismissBoxValue.EndToStart -> { // Swiped Left (Archive)
                                                        onArchive(
                                                            animeDataList[index].id,
                                                            animeDataList[index].isArchived
                                                        )
                                                        true // Allow dismissal
                                                    }

                                                    SwipeToDismissBoxValue.StartToEnd -> { // Swiped Right (Star)
                                                        onStar(
                                                            animeDataList[index].id,
                                                            animeDataList[index].isFavorite
                                                        )
                                                        true // Allow dismissal
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
                                                onStar(
                                                    animeDataList[index].id,
                                                    animeDataList[index].isFavorite
                                                )
                                            },
                                            onArchive = {
                                                onArchive(
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingAnimeExpanded(
    selectedCategory: AnimeCategory,
    onCategorySelected: (AnimeCategory) -> Unit,
    uiState: TrendingAnimeUiState,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    onStar: (id: String, isFavorite: Boolean) -> Unit,
    onArchive: (id: String, isArchived: Boolean) -> Unit,
    expanded: Boolean,
    onSettingsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    vibrantColors: androidx.compose.material3.FloatingToolbarColors,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
    ) {
        SegmentedToggleButton(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
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
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (selectedCategory == AnimeCategory.ALL) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(0.dp),
                                    contentPadding = FabPositioning.LIST_CONTENT_PADDING
                                ) {
                                    items(
                                        animeDataList.size,
                                        key = { animeDataList[it].id }
                                    ) { index ->
                                        val dismissState = rememberSwipeToDismissBoxState(
                                            confirmValueChange = { direction ->
                                                when (direction) {
                                                    SwipeToDismissBoxValue.EndToStart -> { // Swiped Left (Archive)
                                                        onArchive(
                                                            animeDataList[index].id,
                                                            animeDataList[index].isArchived
                                                        )
                                                        true // Allow dismissal
                                                    }

                                                    SwipeToDismissBoxValue.StartToEnd -> { // Swiped Right (Star)
                                                        onStar(
                                                            animeDataList[index].id,
                                                            animeDataList[index].isFavorite
                                                        )
                                                        true // Allow dismissal
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
                                                onStar(
                                                    animeDataList[index].id,
                                                    animeDataList[index].isFavorite
                                                )
                                            },
                                            onArchive = {
                                                onArchive(
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