package com.devpush.animeapp.features.trending.ui.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints // Added
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset // Added
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset // Added
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devpush.animeapp.R
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeCard(
    modifier: Modifier = Modifier,
    anime: AnimeDataEntity,
    onClick: () -> Unit,
    onStar: () -> Unit,
    onArchive: () -> Unit,
    dismissState: SwipeToDismissBoxState // Assuming you pass this from where you use AnimeCard
) {

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = true, // Swipe Right for Star
        enableDismissFromEndToStart = true, // Swipe Left for Archive
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val currentProgress = dismissState.progress // Progress from 0.0 to 1.0

            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.inversePrimary // Star
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error // Archive
                else -> Color.Transparent
            }

            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Filled.Star
                SwipeToDismissBoxValue.EndToStart -> Icons.Filled.Archive
                else -> null
            }

            // Icon scaling based on progress
            val iconScale = lerp(0.75f, 1.25f, currentProgress)
            // Icon alpha based on progress
            val iconAlpha = lerp(0.5f, 1f, currentProgress)

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                // Background Pill
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(percent = 100))
                        .background(color)
                )

                if (icon != null && direction != null) {
                    // Define how far the icon moves. We want it to end up roughly centered in its half.
                    // The icon will start from an edge and move inwards.
                    val iconVisibleTriggerProgress = 0.1f // Start showing/moving icon after this progress
                    val effectiveProgress = if (currentProgress < iconVisibleTriggerProgress) 0f else (currentProgress - iconVisibleTriggerProgress) / (1f - iconVisibleTriggerProgress)

                    // Max distance the icon travels horizontally from its starting edge alignment
                    // This is an arbitrary value, adjust for desired visual effect.
                    // Let's say we want it to move about 1/4 of the container width at full swipe.
                    val maxIconTravelDistance = constraints.maxWidth / 4f

                    val horizontalOffsetPx = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> { // Swiping Right (Star)
                            // Icon starts at left edge and moves right
                            lerp(0f, maxIconTravelDistance, effectiveProgress)
                        }
                        SwipeToDismissBoxValue.EndToStart -> { // Swiping Left (Archive)
                            // Icon starts at right edge and moves left
                            // Negative because it's moving from right to left relative to its initial alignment
                            -lerp(0f, maxIconTravelDistance, effectiveProgress)
                        }
                        else -> 0f
                    }

                    // The initial alignment for the icon's containing Box
                    val iconContainerAlignment = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        else -> Alignment.Center // Should not happen with current logic
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight() // Take full height
                            .align(iconContainerAlignment) // Align the container to start or end
                            .offset { IntOffset(horizontalOffsetPx.roundToInt(), 0) }
                            .padding(horizontal = 24.dp) // Padding for the icon within its moving container
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = when (direction) {
                                SwipeToDismissBoxValue.StartToEnd -> stringResource(R.string.action_star)
                                SwipeToDismissBoxValue.EndToStart -> stringResource(R.string.action_archive)
                                else -> null
                            },
                            modifier = Modifier
                                .align(Alignment.Center) // Center icon in its padded, offsetted box
                                .scale(iconScale),
                            tint = Color.White.copy(alpha = iconAlpha)
                        )
                    }
                }
            }
        }
    ) {
        // This is the actual visible card content
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(0.dp), // Important: No rounding here so swipe background is flush
            // Add elevation or border to the Card if needed for visual separation
            // when it's not being swiped.
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                AsyncImage(
                    model = anime.attributes.posterImage.originalUrl,
                    contentDescription = anime.attributes.canonicalTitle,
                    modifier = Modifier
                        .size(width = 100.dp, height = 120.dp) // Adjusted size for example
                        .clip(RoundedCornerShape(8.dp)), // Slight rounding for the image itself
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            // Example styling for rating, adjust as needed
                            // .background(
                            //    Color(0xFFC4C7EB), // Example color
                            //    shape = RoundedCornerShape(16.dp)
                            // )
                            .padding(horizontal = 0.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, // Assuming this is for displaying current rating
                            contentDescription = stringResource(R.string.rating_icon_description),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary // Example tint
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = anime.attributes.averageRating?.toString() ?: "N/A",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = anime.attributes.canonicalTitle ?: stringResource(R.string.title_unknown),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = anime.attributes.synopsis ?: stringResource(R.string.no_synopsis_available),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2, // Limit lines for synopsis in the card
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// Linear interpolation function
private fun lerp(startValue: Float, endValue: Float, fraction: Float): Float {
    return startValue + fraction * (endValue - startValue)
}

