package com.mmarra.movie.ui.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mmarra.movie.ui.screen.list.components.MovieCard

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Искать фильм...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardActions = KeyboardActions(onSearch = { viewModel.searchMovies(searchQuery) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState.moviesState) {
                is MovieListMoviesState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MovieListMoviesState.Success -> {
                    if (state.movies.isEmpty()) {
                        Text(
                            text = if (uiState.searchQuery != null) "No movies found for '${uiState.searchQuery}'" else "No movies found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            items(state.movies.size) { index ->
                                val movie = state.movies[index]
                                MovieCard(
                                    movie = movie,
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