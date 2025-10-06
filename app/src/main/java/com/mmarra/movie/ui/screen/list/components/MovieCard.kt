package com.mmarra.movie.ui.screen.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mmarra.movie.model.Movie

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = movie.poster?.previewUrl ?: movie.poster?.url,
                contentDescription = movie.name,
                modifier = Modifier
                    .size(80.dp)
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = movie.name ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = movie.year?.toString() ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = movie.genres.joinToString { it.name },
                    style = MaterialTheme.typography.bodySmall
                )
                movie.rating?.let { rating ->
                    Text(
                        text = "KP: ${rating.kp}, IMDb: ${rating.imdb}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}