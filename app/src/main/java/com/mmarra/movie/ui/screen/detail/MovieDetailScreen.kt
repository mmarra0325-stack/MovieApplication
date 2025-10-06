package com.mmarra.movie.ui.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mmarra.movie.model.Movie

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            MovieDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is MovieDetailUiState.Success -> {
                MovieDetailContent(
                    movie = state.movie,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is MovieDetailUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            AsyncImage(
                model = movie.poster?.url,
                contentDescription = movie.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = movie.name ?: "Без названия",
                style = MaterialTheme.typography.headlineMedium
            )
            movie.alternativeName?.let {
                Text(
                    text = "(${it})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            movie.enName?.let {
                Text(
                    text = "EN: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "Год: ${movie.year ?: "Неизвестно"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Страны: ${movie.countries.joinToString { it.name }}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Жанры: ${movie.genres.joinToString { it.name }}",
                style = MaterialTheme.typography.bodyLarge
            )
            movie.rating?.let { rating ->
                Text(
                    text = "Рейтинг: Кинопоиск ${rating.kp ?: "N/A"}, IMDb ${rating.imdb ?: "N/A"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "Возрастной рейтинг: ${movie.ageRating?.let { "$it+" } ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Длительность: ${movie.movieLength ?: "N/A"} мин",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Описание: ${movie.description ?: "Нет описания"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Актеры и создатели: ${movie.persons.joinToString { "${it.name} (${it.profession ?: "unknown"})" }}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}