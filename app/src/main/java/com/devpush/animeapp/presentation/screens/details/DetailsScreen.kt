package com.devpush.animeapp.presentation.screens.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.devpush.animeapp.presentation.screens.details.DetailsScreenViewModel
import org.koin.androidx.compose.koinViewModel

// Imports for the FloatingActionButtonMenu - ensure these are correct
// You might need to adjust these based on where ToggleFloatingActionButton,
// FloatingActionButtonMenu, FloatingActionButtonMenuItem, and animateFloatingActionButton
// are defined in your project.
// Assuming they are in a similar package or accessible globally.
// If not, you'll need to import them from their specific location.
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

    val anime by viewModel.anime.collectAsStateWithLifecycle()

    // State for the LazyColumn in DetailsScreen to control FAB visibility
    val detailsListState = rememberLazyListState()
    val fabVisible by remember {
        derivedStateOf {
            // Show FAB if list is at the top OR if the menu is already expanded
            detailsListState.firstVisibleItemIndex == 0
        }
    }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            val fabMenuItems =
                listOf(
                    Icons.Filled.Snooze to "Snooze",
                    Icons.Filled.Archive to "Archive",
                    Icons.AutoMirrored.Filled.Label to "Label", // Use the correct AutoMirrored import
                )

            FloatingActionButtonMenu(
                modifier = Modifier,
                expanded = fabMenuExpanded,
                button = { // This is the MAIN button that toggles the menu
                    ToggleFloatingActionButton(
                        modifier = Modifier.animateFloatingActionButton( // Apply animation here
                            visible = fabVisible || fabMenuExpanded,
                            alignment = Alignment.BottomEnd // Ensure this alignment is what you want for the button itself
                        ),
                        checked = fabMenuExpanded,
                        containerSize = ToggleFloatingActionButtonDefaults.containerSizeLarge(),
                        onCheckedChange = { fabMenuExpanded = !fabMenuExpanded }
                    ) {
                        // Icon animation for the main Toggle FAB
                        val checkedProgress = animateFloatAsState( // Use animateFloatAsState
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
                if (anime != null) {
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
                                text = anime?.attributes?.startDate?.split("-")?.first() ?: "N/A",
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
                            Text(text = anime?.attributes?.synopsis ?: "No synopsis available.")
                        }
                    }
                } else { // Show loading indicator centered within the content area if anime is null and not just in a box overlaying everything
                    // This 'else' block for the anime != null condition
                    // handles the case where data is loading
                    // The Box overlay for loading indicator can be removed if this is sufficient
                }
            }

            // Add more items to your DetailsScreen LazyColumn if needed
            // for (index in 0 until 5) { // Example: if you had other items
            //     item {
            //         Text(
            //             text = "Details Screen Item - $index",
            //             modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            //         )
            //     }
            // }
        }

        // This loading indicator will overlay the whole screen.
        // Consider if you want it inside the LazyColumn when anime is null,
        // or if this global overlay is preferred.
        if (anime == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Respect scaffold padding
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // Using standard Material 3 indicator
            }
        }
    }
}

// --- Dummy/Placeholder Composables for FloatingActionButtonMenu components ---
// Replace these with your actual implementations or ensure they are correctly imported.
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingActionButtonMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    button: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    // This is a simplified representation. Your actual implementation might be more complex,
    // potentially involving a Box to overlay items or specific layout logic.
    Box(modifier = modifier.fillMaxSize()) { // Ensure it aligns correctly within Scaffold
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), // Common alignment and padding
            horizontalAlignment = Alignment.End
        ) {
            if (expanded) {
                // Material 3 recommendation is often to have menu items in a Surface or similar container
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 4.dp, // Add some elevation
                    modifier = Modifier.padding(bottom = 8.dp) // Space between items and FAB
                ) {
                    Column {
                        content()
                    }
                }
            }
            button() // The main FAB
        }
    }
}

@Composable
fun ToggleFloatingActionButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    containerSize: Dp = 56.dp, // Default FAB size, adjust as needed (e.g., ToggleFloatingActionButtonDefaults.containerSizeLarge())
    onCheckedChange: (Boolean) -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    FloatingActionButton(
        // Using a standard FAB as a base for the toggle
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        // containerColor = if (checked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer, // Example color change
        // elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) { // RowScope for content
            content()
        }
    }
}

// Placeholder for ToggleFloatingActionButtonDefaults if you have one
object ToggleFloatingActionButtonDefaults {
    @Composable
    fun containerSizeLarge(): Dp = 96.dp // Example large size
}

@Composable
fun Modifier.animateFloatingActionButton(
    visible: Boolean,
    alignment: Alignment = Alignment.BottomEnd // Default alignment, can be overridden
): Modifier {
    // This is a placeholder. Your actual animation logic for visibility
    // might involve AnimatingVisibility or other animation APIs.
    // For a simple show/hide based on `visible`, you might not even need a complex modifier.
    // If `visible` is false, you might make the component take no space or set alpha to 0.
    return this.then(
        if (visible) Modifier else Modifier // Simple visibility, or use AnimatingVisibility for transitions
        // .graphicsLayer { alpha = if (visible) 1f else 0f } // Example: fade
    )
}

@Composable
fun Modifier.animateIcon(checkedProgress: () -> Float): Modifier {
    // Placeholder for your icon animation modifier.
    // It might use graphicsLayer for rotation, scaling, etc., based on checkedProgress.
    // val progress = checkedProgress()
    // return this.graphicsLayer(rotationZ = progress * 180f) // Example: rotate icon
    return this
}

@Composable
fun FloatingActionButtonMenuItem(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(16.dp))
        text()
    }
}

// --- End of Dummy/Placeholder Composables ---
// Keep your Preview for DetailsScreen if you have one, or adapt the FloatingActionButtonMenu preview
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun DetailsScreenPreview() {
    // You'll need to mock a ViewModel or pass fake data for a preview
    // For simplicity, I'm passing nulls and a dummy ViewModel.
    // Consider creating a fake ViewModel for previews.
    DetailsScreen(
        id = 1,
        coverImage = "https://via.placeholder.com/600x300.png?text=Anime+Cover",
        // viewModel = // Provide a mock/fake ViewModel here if needed for Preview
    )
}