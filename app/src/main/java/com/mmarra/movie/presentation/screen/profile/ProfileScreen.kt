package com.mmarra.movie.presentation.screen.profile

import android.app.DownloadManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mmarra.movie.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        AsyncImage(
            model = state.photoUri.ifEmpty { R.drawable.empty_photo },
            contentDescription = "photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(130.dp)
                .clip(CircleShape)
        )

        Texts(state.username, state.job)

        Button(
            onClick = {
                val url = state.resumeUrl.trim()

                if (url.isBlank()) {
                    Toast.makeText(
                        context,
                        "Нет ссылки на резюме",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@Button
                }

                try {
                    val uri = url.toUri()
                    val request = DownloadManager.Request(uri)
                        .setTitle("Резюме скачивается")
                        .setDescription(uri.lastPathSegment ?: "Файл")
                        .setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        )
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

                    val manager = context.getSystemService(DownloadManager::class.java)
                    manager.enqueue(request)

                    Toast.makeText(
                        context,
                        "Начало загрузки…",
                        Toast.LENGTH_SHORT,
                    ).show()
                } catch (_: Exception) {
                    Toast.makeText(
                        context,
                        "Неизвестная ошибка",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("Скачать резюме")
        }
    }
}

@Composable
private fun Texts(username: String, job: String) {
    Text(
        text = username.ifEmpty { "Не указали имя" },
        fontSize = 22.sp,
        color = Color.White,
        modifier = Modifier.padding(top = 10.dp)
    )

    Text(
        text = job.ifEmpty { "Нет должности" },
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(top = 10.dp)
    )
}