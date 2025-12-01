package com.mmarra.movie.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {

    @Serializable
    data object Movies : Screen("movies")

    @Serializable
    data object Filters : Screen("filters")

    @Serializable
    data object FavoriteMovies : Screen("favorite_movies")

    @Serializable
    data object Profile : Screen("profile")

    @Serializable
    data object ProfileEdit : Screen("profile_edit")

    @Serializable
    data class MovieDetail(val id: Int) : Screen("movie_detail/$id") {
        companion object {
            const val routeWithArg = "movie_detail/{id}"
        }
    }
}
