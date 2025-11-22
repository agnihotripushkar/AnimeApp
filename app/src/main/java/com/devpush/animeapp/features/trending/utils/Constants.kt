package com.devpush.animeapp.features.trending.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Constants {
    const val TAG = "TrendingAnime"
    const val FabMenuFav = "Favourite"
    const val FabMenuArchive = "Archive"
}

/**
 * Constants for FAB positioning across different layouts
 */
object FabPositioning {
    
    // Content padding to prevent overlap with FAB
    val CONTENT_BOTTOM_PADDING = 80.dp
    
    // Standard content padding values for lists with FAB
    val LIST_CONTENT_PADDING = PaddingValues(bottom = CONTENT_BOTTOM_PADDING)
    
    // Standard content padding values for grids with FAB
    val GRID_CONTENT_PADDING = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        top = 8.dp,
        bottom = CONTENT_BOTTOM_PADDING
    )
}