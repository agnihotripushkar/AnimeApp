package com.devpush.animeapp.features.details.ui

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.devpush.animeapp.R
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.details.data.remote.responsebody.GenreData
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalLayoutApi::class)
@Composable
fun DetailsScreen(
    id: Int,
    coverImage: String?,
    viewModel: DetailsScreenViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchAnime(id)
    }

    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(
            LocalContext.current as Activity
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val detailsListState = rememberLazyListState()
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    when (val state = uiState) {
        is DetailsScreenUiState.Success -> {
            val anime: AnimeDataEntity? = state.animeData
            if (anime != null) {
                Scaffold(
                    floatingActionButton = {
                        val fabMenuItems =
                            listOf(
                                Icons.Filled.Favorite to "Favorite",
                                Icons.Filled.Archive to "Archive",
                            )

                        FloatingActionButtonMenu(
                            modifier = Modifier,
                            expanded = fabMenuExpanded,
                            button = { // This is the MAIN button that toggles the menu
                                ToggleFloatingActionButton(
                                    modifier = Modifier.animateFloatingActionButton( // Apply animation here
                                        visible = true,
                                        alignment = Alignment.BottomEnd
                                    ),
                                    checked = fabMenuExpanded,
                                    // for large size
                                    // containerSize = ToggleFloatingActionButtonDefaults.containerSizeLarge(),
                                    onCheckedChange = { fabMenuExpanded = !fabMenuExpanded }
                                ) {
                                    // Icon animation for the main Toggle FAB
                                    val checkedProgress =
                                        animateFloatAsState( // Use animateFloatAsState
                                            targetValue = if (fabMenuExpanded) 1f else 0f, // Animate between 0 and 1
                                            label = "FabIconProgress"
                                        ).value

                                    val imageVector by remember(checkedProgress) { // Re-evaluate when checkedProgress changes
                                        derivedStateOf {
                                            if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                                        }
                                    }
                                    Icon(
                                        painter = rememberVectorPainter(imageVector),
                                        contentDescription = if (fabMenuExpanded) "Close menu" else "Open menu",
                                        modifier = Modifier.animateIcon({ checkedProgress })
                                    )
                                }
                            }
                        ) { // This is the content of the EXPANDED menu
                            fabMenuItems.forEach { item -> // No need for forEachIndexed if index isn't used
                                FloatingActionButtonMenuItem(
                                    onClick = {
                                        fabMenuExpanded = false // Collapse menu on item click
                                        // TODO: Handle action for item.second (e.g., "Snooze", "Archive")
                                    },
                                    icon = {
                                        Icon(
                                            item.first,
                                            contentDescription = null
                                        )
                                    },
                                    text = { Text(text = item.second) },
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (windowSize == DevicePosture.EXPANDED_WIDTH) {
                            DetailsScreenExpanded(
                                modifier = Modifier.padding(innerPadding),
                                detailsListState = detailsListState,
                                coverImage = coverImage,
                                anime = anime,
                                genres = state.genres
                            )
                        } else {
                            DetailsScreenCompact(
                                modifier = Modifier.padding(innerPadding),
                                detailsListState = detailsListState,
                                coverImage = coverImage,
                                anime = anime,
                                genres = state.genres
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.anime_not_found))
                }
            }
        }

        is DetailsScreenUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message ?: stringResource(R.string.an_error_occurred))
            }

        }

        DetailsScreenUiState.Loading, DetailsScreenUiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ContainedLoadingIndicator()
            }

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsScreenCompact(
    modifier: Modifier,
    detailsListState: LazyListState,
    coverImage: String?,
    anime: AnimeDataEntity,
    genres: List<GenreData>
) {
    LazyColumn(
        state = detailsListState,
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            AsyncImage(
                model = coverImage,
                contentDescription = "Anime Cover Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = anime.attributes.canonicalTitle ?: stringResource(R.string.unknown_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = anime.attributes.startDate?.split("-")?.first()
                            ?: "N/A",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " - ",
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "Rating Star",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = anime?.attributes?.averageRating ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.synopsis),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = anime.attributes.synopsis
                            ?: "No synopsis available."
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genres.forEach { genre ->
                        Button(
                            onClick = { /*TODO*/ },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = genre.attributes.name)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsScreenExpanded(
    modifier: Modifier,
    detailsListState: LazyListState,
    coverImage: String?,
    anime: AnimeDataEntity,
    genres: List<GenreData>
) {
    Row(
        modifier = modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = coverImage,
            contentDescription = "Anime Cover Image",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(9f / 16f)
                .clip(
                    RoundedCornerShape(
                        topEnd = 10.dp,
                        bottomEnd = 10.dp
                    )
                ),
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            state = detailsListState,
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = anime.attributes.canonicalTitle ?: stringResource(R.string.unknown_title),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    )
                    {
                        Text(
                            text = anime.attributes.startDate?.split("-")?.first() ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " - ",
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                        Text(
                            text = anime.attributes.endDate?.split("-")?.first()
                                ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Rating Star",
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Text(
                                text = anime?.attributes?.averageRating ?: "N/A",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Text(
                                text = anime?.attributes?.ageRating ?: "N/A",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = " - ",
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )

                            Text(
                                text = anime?.attributes?.ageRatingGuide ?: "N/A",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Episodes - ${anime.attributes.episodeCount}" ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "Type - ${anime.attributes.subType}" ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genres.forEach { genre ->
                            Button(
                                onClick = { /*TODO*/ },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(text = genre.attributes.name)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.synopsis),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = anime.attributes.synopsis
                                ?: "No synopsis available."
                        )
                    }


                }
            }
        }
    }
}





