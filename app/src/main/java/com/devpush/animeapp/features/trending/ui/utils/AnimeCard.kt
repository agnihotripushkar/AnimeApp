package com.devpush.animeapp.features.trending.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.ui.theme.AnimeAppTheme

@Composable
fun AnimeCard(
    anime: AnimeData,
    onClick: () -> Unit,
    onStar: () -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { direction ->
            when (direction) {
                SwipeToDismissBoxValue.EndToStart -> { // Swiped Left (Archive)
                    onArchive()
                    false // Prevent immediate dismissal
                }

                SwipeToDismissBoxValue.StartToEnd -> { // Swiped Right (Star)
                    onStar()
                    false // Prevent immediate dismissal
                }

                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = true, // Enable swipe right (Star)
        enableDismissFromEndToStart = true, // Enable swipe left (Archive)
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Color.Red.copy(alpha = 0.5f) // Star
                SwipeToDismissBoxValue.EndToStart -> Color.Blue.copy(alpha = 0.5f)  // Archive
                else -> Color.Transparent
            }
            val icon = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Filled.Star
                SwipeToDismissBoxValue.EndToStart -> Icons.Filled.Archive
                else -> null
            }
            val alignment = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                icon?.let {
                    Icon(it, contentDescription = null)
                }
            }
        }
    ) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(0.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                AsyncImage(
                    model = anime.attributes.posterImage.original,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Row(
                        modifier = Modifier
                            .background(
                                Color(0xFFC4C7EB),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = anime.attributes.averageRating.toString())
                    }
                    Text(
                        text = anime.attributes.canonicalTitle.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = anime.attributes.synopsis.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }
    }
}