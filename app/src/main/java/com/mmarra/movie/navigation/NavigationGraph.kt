package com.mmarra.movie.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mmarra.movie.R
import com.mmarra.movie.navigation.components.BottomNavigationBar
import com.mmarra.movie.navigation.components.TopBar
import com.mmarra.movie.ui.screen.detail.MovieDetailScreen
import com.mmarra.movie.ui.screen.list.MovieListScreen

@Composable
fun NavigationGraph() {
    val backStack = remember { mutableStateListOf<Any>(Movies) }

    Scaffold(
        topBar = {
            val currentRoute = backStack.lastOrNull()
            val showBack = currentRoute != Movies || backStack.size > 1
            TopBar(
                titleRes = when (currentRoute) {
                    Movies -> R.string.movies_title
                    is MovieDetail -> R.string.movie_detail_title
                    else -> R.string.movie_not_found
                },
                showBackButton = showBack,
                onBackClick = { backStack.removeLastOrNull() }
            )
        },
        bottomBar = {
            val currentRoute = backStack.lastOrNull()
            if (currentRoute == Movies) {
                BottomNavigationBar(
                    items = listOf(BottomNavItem.Movies),
                    selectedItem = BottomNavItem.Movies,
                    onItemSelected = {  }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is Movies -> NavEntry(key) {
                        MovieListScreen(
                            onMovieClick = { movieId ->
                                backStack.add(MovieDetail(movieId))
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }


                    is MovieDetail -> NavEntry(key) {
                        MovieDetailScreen(
                            movieId = key.id,
                            modifier = Modifier
                        )
                    }

                    else -> NavEntry(Unit) { Text("Unknown route") }
                }
            }
        )
    }
}
