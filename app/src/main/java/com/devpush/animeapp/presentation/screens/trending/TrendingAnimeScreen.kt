package com.devpush.animeapp.presentation.screens.trending

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.presentation.components.AnimeCard

@Composable
fun TrendingAnimeScreen(
    onAnimeClick: (String,String) -> Unit,
    viewModel: TrendingAnimeViewModel = koinViewModel()
) {

    val animeData by viewModel.animeData.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        AnimatedContent(
            targetState = animeData.isEmpty(),
            label = ""
        ) { isEmpty ->
            if (isEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding() + 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item{
                        Text(
                            text = "Trending Anime",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    items(animeData.size) { index ->
                        AnimeCard(
                            anime = animeData[index],
                            onClick = {
                                onAnimeClick(
                                    animeData[index].attributes.posterImage.original,
                                    animeData[index].id
                                )
                            }
                        )
                    }
                }

                }
            }

        }


    }