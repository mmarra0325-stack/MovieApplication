package com.mmarra.movie.ui.screen.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mmarra.movie.model.Movie
import com.mmarra.movie.model.displayTitle
import com.mmarra.movie.ui.screen.detail.components.InfoChip
import com.mmarra.movie.ui.screen.detail.components.PersonCard
import com.mmarra.movie.ui.screen.detail.components.RatingChip
import com.mmarra.movie.ui.screen.detail.components.SimilarMovieCard
import com.mmarra.movie.ui.screen.detail.components.TopBadge
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MovieDetailScreen(
    movieId: Int,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            MovieDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is MovieDetailUiState.Success -> {
                MovieDetailContent(movie = state.movie, modifier = modifier)
            }

            is MovieDetailUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(movie: Movie, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                val backdrop = movie.backdrop?.url ?: movie.poster?.url
                AsyncImage(
                    model = backdrop,
                    contentDescription = "Backdrop ${movie.displayTitle()}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                ),
                                startY = 180f
                            )
                        )
                )

                movie.logo?.url?.let { logoUrl ->
                    AsyncImage(
                        model = logoUrl,
                        contentDescription = "Logo",
                        modifier = Modifier
                            .padding(12.dp)
                            .size(64.dp)
                            .align(Alignment.TopStart),
                        contentScale = ContentScale.Fit
                    )
                }

                movie.poster?.previewUrl?.let { posterPreview ->
                    Card(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp)
                            .align(Alignment.BottomStart)
                            .width(120.dp)
                            .height(180.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        AsyncImage(
                            model = posterPreview,
                            contentDescription = "Poster",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = movie.displayTitle(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                val subtitleParts = mutableListOf<String>()
                movie.alternativeName?.takeIf { it.isNotBlank() && it != movie.displayTitle() }
                    ?.let { subtitleParts.add(it) }
                movie.enName?.takeIf { it.isNotBlank() && it != movie.displayTitle() }
                    ?.let { subtitleParts.add(it) }
                movie.year?.let { subtitleParts.add(it.toString()) }
                if (subtitleParts.isNotEmpty()) {
                    Text(
                        text = subtitleParts.joinToString(" • "),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                movie.slogan?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = "\"$it\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                movie.status?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = "Статус: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    movie.rating?.kp?.let {
                        RatingChip(
                            rating = it,
                            label = "Кинопоиск",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    movie.rating?.imdb?.let {
                        RatingChip(rating = it, label = "IMDb", color = Color(0xFFF5C518))
                    }
                    movie.rating?.tmdb?.let {
                        RatingChip(
                            rating = it,
                            label = "TMDB",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (movie.rating == null || (movie.rating.kp == null && movie.rating.imdb == null && movie.rating.tmdb == null)) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "Нет оценок",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    movie.top10?.let { TopBadge("Top10", it.toString()) }
                    movie.top250?.let { TopBadge("Top250", it.toString()) }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { InfoChip("${movie.year ?: "?"}", Icons.Default.CalendarToday) }
                    item { InfoChip("${movie.movieLength ?: "?"} мин", Icons.Default.Schedule) }
                    item {
                        InfoChip(movie.ageRating?.let { "$it+" } ?: "N/A",
                            Icons.Default.Person)
                    }
                    item {
                        InfoChip(
                            movie.genres.take(3).joinToString { it.name }.ifBlank { "—" },
                            Icons.Default.TheaterComedy
                        )
                    }
                    item {
                        InfoChip(
                            movie.countries.take(3).joinToString { it.name }.ifBlank { "—" },
                            Icons.Default.Public
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    movie.createdAt?.let {
                        Text(
                            text = "Создано: ${formatInstant(it)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    movie.updatedAt?.let {
                        Text(
                            text = "Обновлено: ${formatInstant(it)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            movie.shortDescription?.takeIf { it.isNotBlank() }?.let { short ->
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Кратко",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = short,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = movie.description ?: "Описание отсутствует",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                )
            }
        }

        item {
            movie.videos?.trailers?.takeIf { it.isNotEmpty() }?.let { trailers ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Трейлеры",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    trailers.forEach { trailer ->
                        Text(
                            text = trailer.name ?: trailer.url ?: "Trailer",
                            modifier = Modifier
                                .clickable {
                                    trailer.url?.let { url ->
                                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                        context.startActivity(intent)
                                    }
                                }
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Divider()
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Актеры и создатели",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(movie.persons) { person ->
                        PersonCard(person = person)
                    }
                }
            }
        }

        item {
            movie.similarMovies.takeIf { it.isNotEmpty() }?.let { list ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Похожие фильмы",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(list) { similarMovie ->
                            SimilarMovieCard(movie = similarMovie)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun formatInstant(inst: Instant): String {
    return try {
        val z = inst.atZone(ZoneId.systemDefault())
        val fmt = DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneId.systemDefault())
        fmt.format(z)
    } catch (e: Exception) {
        inst.toString()
    }
}
