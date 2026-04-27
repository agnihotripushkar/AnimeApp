package com.devpush.animeapp.features.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R
import com.devpush.animeapp.core.presentation.UiText
import com.devpush.animeapp.features.trending.ui.utils.AnimeCard
import com.devpush.animeapp.features.trending.utils.FabPositioning

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun HomeAnimeCompact(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onAnimeClick: (posterImage: String?, animeId: String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit = {},
    onSettingsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    vibrantColors: androidx.compose.material3.FloatingToolbarColors,
    scaffoldPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                (fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.92f, animationSpec = tween(300))).togetherWith(
                    fadeOut(animationSpec = tween(300))
                )
            },
            label = "Content State Transition"
        ) { currentState ->
            when {
                currentState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ContainedLoadingIndicator()
                    }
                }
                currentState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (val e = currentState.error) {
                                    is UiText.DynamicString -> e.value
                                    is UiText.StringResource -> stringResource(e.id)
                                    else -> "Unknown error"
                                },
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onAction(HomeAction.Retry) }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
                currentState.animeList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_trending_anime_found))
                    }
                }
                else -> {
                    val animeList = currentState.animeList
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        contentPadding = FabPositioning.LIST_CONTENT_PADDING
                    ) {
                        items(animeList.size, key = { animeList[it].id }) { index ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { direction ->
                                    when (direction) {
                                        SwipeToDismissBoxValue.EndToStart -> {
                                            onAction(HomeAction.ArchiveAnime(animeList[index].id, animeList[index].isArchived))
                                            true
                                        }
                                        SwipeToDismissBoxValue.StartToEnd -> {
                                            onAction(HomeAction.StarAnime(animeList[index].id, animeList[index].isFavorite))
                                            true
                                        }
                                        SwipeToDismissBoxValue.Settled -> false
                                    }
                                }
                            )
                            AnimeCard(
                                modifier = Modifier.animateItem(),
                                anime = animeList[index],
                                onClick = {
                                    onAnimeClick(
                                        animeList[index].attributes.posterImage.originalUrl,
                                        animeList[index].id
                                    )
                                },
                                onStar = { onAction(HomeAction.StarAnime(animeList[index].id, animeList[index].isFavorite)) },
                                onArchive = { onAction(HomeAction.ArchiveAnime(animeList[index].id, animeList[index].isArchived)) },
                                dismissState = dismissState
                            )
                        }
                    }
                }
            }
        }

        HorizontalFloatingToolbar(
            expanded = expanded,
            floatingActionButton = {
                FloatingToolbarDefaults.VibrantFloatingActionButton(
                    onClick = { onExpandedChange(!expanded) }
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null)
                }
            },
            colors = vibrantColors,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            content = {
                IconButton(onClick = onArchiveClick) {
                    Icon(Icons.Filled.Archive, contentDescription = stringResource(R.string.archive))
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(Icons.Filled.Star, contentDescription = stringResource(R.string.favorite))
                }
            }
        )
    }
}
