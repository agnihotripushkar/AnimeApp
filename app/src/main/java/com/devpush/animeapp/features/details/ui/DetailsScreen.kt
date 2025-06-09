package com.devpush.animeapp.features.details.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.devpush.animeapp.domian.model.AnimeData
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailsScreen(
    id: Int,
    coverImage: String?,
    viewModel: DetailsScreenViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchAnime(id)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // State for the LazyColumn in DetailsScreen to control FAB visibility
    val detailsListState = rememberLazyListState()
//    val fabVisible by remember {
//        derivedStateOf {
//            // Show FAB if list is at the top OR if the menu is already expanded
//            detailsListState.firstVisibleItemIndex == 0
//        }
//    }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    when (val state = uiState) {
        is DetailsScreenUiState.Success -> {
            val anime: AnimeData? = state.animeData
            if (anime != null) {
                Scaffold(
                    floatingActionButton = {
                        val fabMenuItems =
                            listOf(
                                Icons.Filled.Bookmark to "BookMark",
                                Icons.Filled.Favorite to "Favorite",
                                Icons.Filled.Archive to "Archive", // Use the correct AutoMirrored import
                            )

                        FloatingActionButtonMenu(
                            modifier = Modifier,
                            expanded = fabMenuExpanded,
                            button = { // This is the MAIN button that toggles the menu
                                ToggleFloatingActionButton(
                                    modifier = Modifier.animateFloatingActionButton( // Apply animation here
                                        visible = true,
                                        alignment = Alignment.BottomEnd // Ensure this alignment is what you want for the button itself
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
                                        contentDescription = if (fabMenuExpanded) "Close menu" else "Open menu", // Add Content Description
                                        modifier = Modifier.animateIcon({ checkedProgress }) // Pass the animated progress
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
                                    }, // Add specific content descriptions if possible
                                    text = { Text(text = item.second) },
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End // Standard position for FAB
                ) { innerPadding ->
                    LazyColumn(
                        state = detailsListState, // Use the state here
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), // Apply innerPadding from Scaffold
                        horizontalAlignment = Alignment.Start
                    ) {
                        item {
                            AsyncImage(
                                model = coverImage,
                                contentDescription = "Anime Cover Image", // Add Content Description
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
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
                                    text = anime?.attributes?.canonicalTitle ?: "Unknown Title",
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = anime?.attributes?.startDate?.split("-")?.first()
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
                                            contentDescription = "Rating Star", // Add Content Description
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
                                        text = "Synopsis",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp)) // Add some space
                                    Text(
                                        text = anime?.attributes?.synopsis
                                            ?: "No synopsis available."
                                    )
                                }
                            }
                        }
                    }

                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Anime not found")
                }
            }
        }

        is DetailsScreenUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message ?: "An error occurred")
            }

        }

        DetailsScreenUiState.Loading, DetailsScreenUiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ContainedLoadingIndicator()
            }

        }
    }
}





