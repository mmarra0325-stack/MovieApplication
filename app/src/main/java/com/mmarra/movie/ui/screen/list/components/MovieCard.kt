package com.mmarra.movie.ui.screen.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.mmarra.movie.model.Movie
import com.mmarra.movie.model.displayTitle

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val imageUrl = movie.poster?.previewUrl
                ?: movie.poster?.url
                ?: movie.backdrop?.previewUrl
                ?: movie.backdrop?.url

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
            ) {
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .size(coil.size.Size.ORIGINAL)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = movie.displayTitle(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = movie.displayTitle()
                                    .takeIf { it.length <= 18 }
                                    ?: (movie.displayTitle().take(18) + "…"),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = movie.displayTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                movie.alternativeName?.takeIf { it.isNotBlank() && it != movie.displayTitle() }
                    ?.let { alt ->
                        Text(
                            text = alt,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                movie.enName?.takeIf { it.isNotBlank() && it != movie.displayTitle() }?.let { en ->
                    Text(
                        text = en,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (movie.genres.isNotEmpty()) {
                    Text(
                        text = movie.genres.joinToString { it.name },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = listOfNotNull(
                        movie.year?.toString(),
                        movie.movieLength?.let { "$it мин" }
                    ).joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val ratings = listOf(
                    movie.rating?.kp to "KP",
                    movie.rating?.imdb to "IMDb",
                    movie.rating?.tmdb to "TMDB",
                    movie.rating?.filmCritics to "Critics",
                    movie.rating?.russianFilmCritics to "Росс. критики",
                    movie.rating?.await to "Await"
                ).filter { it.first != null && it.first!! > 0.0 }

                if (ratings.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(ratings.size) { index ->
                            val (value, label) = ratings[index]
                            RatingChipMini(value!!, label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingChipMini(rating: Double, label: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.small
    ) {
        Row(modifier = Modifier
            .padding(vertical = 2.dp)
            .padding(end = 6.dp)) {
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = " $label",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
