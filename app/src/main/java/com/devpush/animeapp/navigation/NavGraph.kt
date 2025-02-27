package com.devpush.animeapp.navigation

sealed class NavGraph(val route: String) {
    data object Welcome: NavGraph(route = "welcome_screen")
    data object Login: NavGraph(route = "login_screen")
    data object Registration: NavGraph(route = "registration_screen")
    data object Home: NavGraph(route = "home_screen")
    data object OnBoarding: NavGraph(route = "onBoarding_screen")
    data object TrendingAnime: NavGraph(route = "trending_anime_screen")
}