package com.devpush.animeapp.utils

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

enum class DevicePosture {
    PHONE_COMPACT,
    TABLET_COMPACT_PORTRAIT, // Compact width but taller (tablet-like)
    MEDIUM_WIDTH,
    EXPANDED_WIDTH
}

@Composable
fun rememberDevicePosture(windowSizeClass: WindowSizeClass): DevicePosture {
    return remember(windowSizeClass) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                // Now also consider height for Compact width
                when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.Expanded -> DevicePosture.TABLET_COMPACT_PORTRAIT // Tablet in portrait
                    else -> DevicePosture.PHONE_COMPACT // Phone in portrait or smaller height device
                }
            }
            WindowWidthSizeClass.Medium -> DevicePosture.MEDIUM_WIDTH
            WindowWidthSizeClass.Expanded -> DevicePosture.EXPANDED_WIDTH
            else -> DevicePosture.PHONE_COMPACT
        }
    }
}