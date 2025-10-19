package com.mmarra.movie.ui.screen.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mmarra.movie.model.SimilarMovie

@Composable
fun SimilarMovieCard(movie: SimilarMovie) {
    Card(
        modifier = Modifier.width(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            movie.poster?.previewUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = movie.name,
                    modifier = Modifier
                        .width(104.dp)
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: Surface(
                modifier = Modifier
                    .width(104.dp)
                    .height(140.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {}
            Text(
                text = movie.name ?: movie.alternativeName ?: movie.enName ?: "—",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            movie.rating?.kp?.let { r ->
                Text(
                    text = String.format("%.1f", r),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}