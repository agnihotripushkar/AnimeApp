@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)
package com.devpush.animeapp.features.trending.ui.utils

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.devpush.animeapp.R
import com.devpush.animeapp.features.trending.utils.Constants
import kotlin.math.exp

@Composable
fun FabMenu(modifier: Modifier = Modifier,
            onClick: (FabItem) -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = { expanded = it }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if(expanded) R.drawable.outline_close_24 else R.drawable.baseline_add_24
                    ),
                    contentDescription = null,
                    tint = if(expanded) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }
    ) {
        items.forEach { item ->
            FloatingActionButtonMenuItem(
                onClick = {
                    onClick(item)
                    expanded = false },
                text = { Text(item.text) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.iconRes),
                        contentDescription = null
                    )
                },
            )
        }
    }
}

val items = listOf(
    FabItem(
        text = Constants.FabMenuFav,
        iconRes = R.drawable.outline_star_outline_24
    ),
    FabItem(
        text = Constants.FabMenuArchive,
        iconRes = R.drawable.outline_archive_24
    ),
    FabItem(
        text = Constants.FabMenuSettings,
        iconRes = R.drawable.outline_build_24
    ),
    FabItem(
        text = Constants.FabMenuRecommended,
        iconRes = R.drawable.outline_recommend_24
    ),
)

data class FabItem(
    val text: String,
    val iconRes: Int
)