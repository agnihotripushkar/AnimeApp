package com.devpush.animeapp.core.navigation // Or your actual package

import android.net.Uri
import com.devpush.animeapp.utils.Constants.LOGIN_SCREEN
import com.devpush.animeapp.utils.Constants.ONBOARDING_SCREEN
import com.devpush.animeapp.utils.Constants.REGISTRATION_SCREEN
import com.devpush.animeapp.utils.Constants.SETTINGS_SCREEN
import com.devpush.animeapp.utils.Constants.TRENDING_ANIME_SCREEN

sealed class NavGraph(val route: String) {
    data object Login : NavGraph(route = LOGIN_SCREEN)
    data object Registration : NavGraph(route = REGISTRATION_SCREEN)
    data object OnBoarding : NavGraph(route = ONBOARDING_SCREEN)
    data object TrendingAnime : NavGraph(route = TRENDING_ANIME_SCREEN)
    data object Settings : NavGraph(route = SETTINGS_SCREEN)

    // For routes with arguments
    data object DetailAnime : NavGraph(route = "detail_anime/{coverurl}/{id}") {
        fun createRoute(coverUrl: String, animeId: Any): String {
            val encodedUrl = Uri.encode(coverUrl)
            return "detail_anime/$encodedUrl/${animeId}"
        }
    }
}