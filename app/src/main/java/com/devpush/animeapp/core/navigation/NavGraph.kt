package com.devpush.animeapp.core.navigation // Or your actual package

import android.net.Uri

sealed class NavGraph(val route: String) {
    data object Login : NavGraph(route = "login_screen")
    data object Registration : NavGraph(route = "registration_screen")
    data object OnBoarding : NavGraph(route = "onBoarding_screen")
    data object TrendingAnime : NavGraph(route = "trending_anime_screen")

    // For routes with arguments
    data object DetailAnime : NavGraph(route = "detail_anime/{coverurl}/{id}") {
        fun createRoute(coverUrl: String, animeId: Any): String {
            val encodedUrl = Uri.encode(coverUrl)
            return "detail_anime/$encodedUrl/${animeId}"
        }
    }
}