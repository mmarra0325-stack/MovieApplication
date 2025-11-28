package com.mmarra.movie.presentation.screen.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mmarra.movie.presentation.ui_kit.MovieCard

@Composable
fun FavoriteMoviesScreen(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteMoviesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.emptyMoviesList -> {
                Text(
                    text = "Нет избранных фильмов",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    items(uiState.movies) { movie ->

                        MovieCard(
                            movie = movie,
                            isFavorite = true,
                            onToggleFavorite = {
                                viewModel.removeFromFavorites(movie.id)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onMovieClick(movie.id) }
                        )
                    }
                }
            }
        }
    }
}