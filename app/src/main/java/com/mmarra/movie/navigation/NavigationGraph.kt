package com.mmarra.movie.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mmarra.movie.R
import com.mmarra.movie.navigation.components.BottomNavigationBar
import com.mmarra.movie.navigation.components.TopBar
import com.mmarra.movie.presentation.screen.detail.MovieDetailScreen
import com.mmarra.movie.presentation.screen.favorites.FavoriteMoviesScreen
import com.mmarra.movie.presentation.screen.filters.FiltersScreen
import com.mmarra.movie.presentation.screen.list.MovieListScreen
import com.mmarra.movie.presentation.screen.profile.ProfileEditScreen
import com.mmarra.movie.presentation.screen.profile.ProfileScreen

@Composable
fun NavigationGraph() {

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(navController)
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Movies.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Movies.route) {
                MovieListScreen(
                    onMovieClick = { id ->
                        navController.navigate(Screen.MovieDetail(id).route)
                    },
                    onOpenFilters = { navController.navigate(Screen.Filters.route) }
                )
            }

            composable(Screen.Filters.route) {
                FiltersScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.FavoriteMovies.route) {
                FavoriteMoviesScreen(
                    onMovieClick = { id ->
                        navController.navigate(Screen.MovieDetail(id).route)
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(Screen.ProfileEdit.route) {
                ProfileEditScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.MovieDetail.routeWithArg,
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                ),
            ) { entry ->
                val id = entry.arguments!!.getInt("id")
                MovieDetailScreen(movieId = id)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem.Movies,
        BottomNavItem.FavoriteMovies,
        BottomNavItem.Profile,
    )

    if (currentRoute?.startsWith("movie_detail") == true) {
        return
    }

    BottomNavigationBar(
        items = items,
        selectedItem = items.firstOrNull { it.route == currentRoute } ?: items.first(),
        onItemSelected = { item ->
            if (item.route != currentRoute) {
                navController.navigate(item.route) {
                    popUpTo(Screen.Movies.route)
                    launchSingleTop = true
                }
            }
        }
    )
}

@Composable
fun TopBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val showBack = navController.previousBackStackEntry != null

    val titleRes = when (backStackEntry?.destination?.route) {
        Screen.Movies.route -> R.string.movies_title
        Screen.FavoriteMovies.route -> R.string.favorite_movies_title
        Screen.Profile.route -> R.string.profile_title
        Screen.ProfileEdit.route -> R.string.profile_edit_title
        Screen.MovieDetail.routeWithArg -> R.string.movie_detail_title
        else -> R.string.app_name
    }

    TopBar(
        titleRes = titleRes,
        showBackButton = showBack,
        showEditButton = backStackEntry?.destination?.route == Screen.Profile.route,
        onBackClick = { navController.popBackStack() },
        onEditClick = { navController.navigate(Screen.ProfileEdit.route) }
    )
}