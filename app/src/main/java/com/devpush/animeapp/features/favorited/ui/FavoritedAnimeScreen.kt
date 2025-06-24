package com.devpush.animeapp.features.favorited.ui

import android.R.attr.navigationIcon
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
import androidx.compose.material3.CircularWavyProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devpush.animeapp.R
import com.devpush.animeapp.core.navigation.NavGraph
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.trending.ui.utils.AnimeCard
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FavoritedAnimeScreen(
    navController: NavController,
    onAnimeClick: (animeId: String) -> Unit = { Timber.d("Anime card clicked: $it") },
    viewModel: FavoritedAnimeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorited_anime)) },
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
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                ContainedLoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (uiState.error != null) {
                Text("Error: ${uiState.error}")
            } else if (uiState.animes.isEmpty()) {
                Text(stringResource(R.string.no_favorited_animes_yet))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp), // Adjusted padding
                    verticalArrangement = Arrangement.spacedBy(0.dp) // AnimeCard might have internal padding
                ) {
                    items(uiState.animes, key = { anime -> anime.id })
                    { anime: AnimeDataEntity ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { direction ->
                                when (direction) {
                                    SwipeToDismissBoxValue.EndToStart -> false


                                    SwipeToDismissBoxValue.StartToEnd -> { // Swiped Right (Star)
                                        viewModel.starAnime(
                                            anime.id,
                                            anime.isFavorite
                                        )
                                        false // Prevent immediate dismissal
                                    }

                                    SwipeToDismissBoxValue.Settled -> false
                                }
                            }
                        )
                        AnimeCard(
                            anime = anime,
                            onClick = { onAnimeClick(anime.id) },
                            onStar = {
                                // On this screen, "starring" effectively means "unfavorite"
                                // anime.isFavorite should be true here
                                viewModel.toggleFavoriteStatus(anime.id, anime.isFavorite)
                            },
                            onArchive = {
                                // Archive action not implemented for this screen
                                Timber.d("Archive clicked for ${anime.id} on favorites screen")
                            },
                            dismissState = dismissState
                        )
                    }
                }
            }
        }
    }
}