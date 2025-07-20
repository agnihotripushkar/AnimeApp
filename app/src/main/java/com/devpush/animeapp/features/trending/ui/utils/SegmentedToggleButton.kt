package com.devpush.animeapp.features.trending.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.ui.AnimeCategory
import com.devpush.animeapp.ui.theme.AnimeAppTheme

@Composable
fun SegmentedToggleButton(
    selectedCategory: AnimeCategory,
    onCategorySelected: (AnimeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // All button
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = if (selectedCategory == AnimeCategory.ALL) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onCategorySelected(AnimeCategory.ALL) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.all_anime),
                color = if (selectedCategory == AnimeCategory.ALL) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontSize = 14.sp,
                fontWeight = if (selectedCategory == AnimeCategory.ALL) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        }

        // Trending button
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = if (selectedCategory == AnimeCategory.TRENDING) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onCategorySelected(AnimeCategory.TRENDING) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.trending),
                color = if (selectedCategory == AnimeCategory.TRENDING) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontSize = 14.sp,
                fontWeight = if (selectedCategory == AnimeCategory.TRENDING) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedToggleButtonPreview() {
    AnimeAppTheme {
        SegmentedToggleButton(selectedCategory = AnimeCategory.TRENDING, onCategorySelected = {})
    }

}
