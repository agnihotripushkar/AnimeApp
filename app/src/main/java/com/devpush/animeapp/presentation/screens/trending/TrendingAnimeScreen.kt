package com.devpush.animeapp.presentation.screens.trending

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.presentation.components.AnimeCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingAnimeScreen(
    onAnimeClick: (String,String) -> Unit,
    viewModel: TrendingAnimeViewModel = koinViewModel()
) {

    val animeData by viewModel.animeData.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }
    val menuItems = listOf("Settings", "Logout")
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Trending Anime") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(
                        onClick = {showMenu = !showMenu}
                    ) {
                        Icon(Icons.Filled.MoreVert,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        menuItems.forEach { menuItem ->
                            DropdownMenuItem(
                                text = { Text(menuItem) },
                                onClick = {
                                    showMenu = false
                                    // Handle menu item click
                                    when (menuItem) {
                                        "Settings" -> {
                                            // Handle settings click
                                            Toast.makeText(context, "Settings clicked", Toast.LENGTH_SHORT).show()
                                        }

                                        "Logout" -> {
                                            // Handle logout click
                                            Toast.makeText(context, "Logout clicked", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = animeData.isEmpty(),
            label = ""
        ) { isEmpty ->
            if (isEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ContainedLoadingIndicator()
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
//                    item{
//                        Text(
//                            text = "Trending Anime",
//                            style = MaterialTheme.typography.displaySmall,
//                            fontWeight = FontWeight.Bold,
//                        )
//                    }
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