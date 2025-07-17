package com.devpush.animeapp.features.trending.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devpush.animeapp.data.local.entities.AnimeAttributesDb
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.data.local.entities.PosterImageDb
import com.devpush.animeapp.ui.theme.AnimeAppTheme

@Composable
fun AnimePoster(
    anime: AnimeDataEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Add some elevation
    ) {
        Column {
            AsyncImage(
                model = anime.attributes.posterImage?.originalUrl.takeIf { !it.isNullOrEmpty() } ?: "https://via.placeholder.com/196x296?text=No+Image",
                contentDescription = anime.attributes.canonicalTitle,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(196f / 296f)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = anime.attributes.canonicalTitle ?: "Unknown Title",
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Anime Poster Preview")
@Composable
fun AnimPosterPreview() {
    AnimeAppTheme {
        AnimePoster(
            anime = AnimeDataEntity(
                id = "1",
                attributes = AnimeAttributesDb(
                    canonicalTitle = "Attack on Titan",
                    posterImage = PosterImageDb(
                        originalUrl = "https://media.kitsu.io/anime/poster_images/7442/original.jpg"
                    ),
                    synopsis = "",
                    averageRating = "",
                    startDate = "2025-09-19",
                    endDate = "2025-10-20",
                    subType = "TV",
                    status = "Finished",
                    ageRating = "R",
                    ageRatingGuide = "Recommended 18+",
                    episodeCount = 25,
                )
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Anime Poster Missing Data Preview")
@Composable
fun AnimPosterMissingDataPreview() {
    AnimeAppTheme {
        AnimePoster(
            anime = AnimeDataEntity(
                id = "2",
                attributes = AnimeAttributesDb(
                    canonicalTitle = null,
                    posterImage = PosterImageDb(
                        originalUrl = ""
                    ),
                    synopsis = "",
                    averageRating = "",
                    startDate = "",
                    endDate = "",
                    subType = "",
                    status = "",
                    ageRating = "",
                    ageRatingGuide = "",
                    episodeCount = 0,
                )
            ),
            onClick = {}
        )
    }
}
