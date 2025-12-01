package com.mmarra.movie.presentation.screen.list

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mmarra.movie.presentation.ui_kit.MovieCard

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                viewModel.onPermissionGranted()
            } else {
                viewModel.onSkipPermission()
            }
        }

    if (uiState.showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onSkipPermission() },
            title = { Text("Разрешите отправку уведомления") },
            text = { Text("Разрешите нам отправлять вам уведомления о выходе новых фильмов") },
            confirmButton = {
                Text(
                    text = "Разрешить",
                    modifier = Modifier
                        .clickable {
                            requestPermissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                        .padding(12.dp)
                )
            },
            dismissButton = {
                Text(
                    text = "Позже",
                    modifier = Modifier
                        .clickable { viewModel.onSkipPermission() }
                        .padding(12.dp)
                )
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Искать фильм...") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                keyboardActions = KeyboardActions(
                    onSearch = { viewModel.searchMovies() }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                trailingIcon = {
                    if (uiState.searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                }
            )

            val hasActiveFilters = uiState.filters.genre.isNotBlank() ||
                    uiState.filters.year != null || uiState.filters.rating != null

            BadgedBox(
                badge = {
                    if (hasActiveFilters) {
                        Badge(
                            modifier = Modifier.size(14.dp),
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            ) {
                IconButton(onClick = onOpenFilters) {
                    Icon(Icons.Default.FilterAlt, contentDescription = "Фильтры")
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState.moviesState) {
                is MovieListMoviesState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MovieListMoviesState.Success -> {
                    if (state.movies.isEmpty()) {
                        Text(
                            text = if (uiState.searchQuery != "") {
                                "No movies found for '${uiState.searchQuery}'"
                            } else {
                                "No movies found"
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            items(state.movies.size) { index ->
                                val movie = state.movies[index]

                                val isFavorite = movie.id in uiState.favoriteIds

                                MovieCard(
                                    movie = movie,
                                    isFavorite = isFavorite,
                                    onToggleFavorite = {
                                        viewModel.toggleFavorite(movie)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .clickable { onMovieClick(movie.id) }
                                )
                            }

                            item {
                                if (uiState.isLoadingNextPage) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }

                        val shouldLoadNext by remember {
                            derivedStateOf {
                                val layoutInfo = listState.layoutInfo
                                val totalItems = layoutInfo.totalItemsCount
                                val lastVisible =
                                    layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                lastVisible >= totalItems - 5 && !uiState.isLoadingNextPage
                            }
                        }

                        LaunchedEffect(shouldLoadNext) {
                            if (shouldLoadNext) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }

                is MovieListMoviesState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}