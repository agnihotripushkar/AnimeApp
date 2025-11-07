package com.devpush.animeapp.features.recommendation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.AnimeCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecommendationScreen(
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
) {
    val viewModel: RecommendationViewModel = koinViewModel()
    val recommendedAnime by viewModel.recommendedAnime.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecommendations()
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                recommendedAnime.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(recommendedAnime) { anime ->
                            AnimeCard(
                                anime = anime,
                                onAnimeClick = {
                                    onAnimeClick(
                                        anime.attributes.posterImage.originalUrl,
                                        anime.id
                                    )
                                },
                                onToggleFavorite = { },
                                onToggleArchive = { }
                            )
                        }
                    }
                }
            }
        }
    }
}
