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
}