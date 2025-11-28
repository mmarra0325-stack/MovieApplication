package com.mmarra.movie.navigation

import com.mmarra.movie.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val title: Int
) {
    data object Movies : BottomNavItem(
        route = "movies",
        icon = R.drawable.ic_movies,
        title = R.string.movies_title
    )

    data object FavoriteMovies : BottomNavItem(
        route = "favorite_movies",
        icon = R.drawable.ic_favorite_movies,
        title = R.string.favorite_movies_title
    )
}