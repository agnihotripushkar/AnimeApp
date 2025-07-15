package com.devpush.animeapp.utils

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

enum class WindowSize { COMPACT, MEDIUM, EXPANDED }

@Composable
fun rememberWindowSizeClass(windowSizeClass: WindowSizeClass): WindowSize {
    var windowSize by remember { mutableStateOf(WindowSize.COMPACT) }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> windowSize = WindowSize.COMPACT
        WindowWidthSizeClass.Medium -> windowSize = WindowSize.MEDIUM
        WindowWidthSizeClass.Expanded -> windowSize = WindowSize.EXPANDED
        else -> windowSize = WindowSize.COMPACT
    }

    return windowSize
}