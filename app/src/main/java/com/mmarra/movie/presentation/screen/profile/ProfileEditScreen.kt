package com.mmarra.movie.presentation.screen.profile

import android.Manifest
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mmarra.movie.R
import java.io.OutputStream
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let { bitmap ->
            val uri = saveBitmap(
                context,
                bitmap
            )
            uri?.let { viewModel.onPhotoUriChange(it.toString()) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
                )
            } catch (e: SecurityException) {
                Toast.makeText(context, "$e", Toast.LENGTH_LONG).show()
            }

            viewModel.onPhotoUriChange(it.toString())
        }
    }

    val permissionLauncherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                "Нет доступа к камере",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    val permissionLauncherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            galleryLauncher.launch(arrayOf("image/*"))
        } else {
            Toast.makeText(
                context,
                "Нет доступа к галерее",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = state.photoUri.ifEmpty { R.drawable.empty_photo },
            contentDescription = "user photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(130.dp)
                .clip(CircleShape)
                .clickable { showDialog = true }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Выберите откуда загрузить изображение") },
                text = {
                    Column {
                        Text(
                            text = "Камера",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val cameraPermission = Manifest.permission.CAMERA
                                    permissionLauncherCamera.launch(cameraPermission)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )

                        Text(
                            text = "Галерея",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val storagePermission =
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        else
                                            Manifest.permission.READ_EXTERNAL_STORAGE

                                    permissionLauncherGallery.launch(storagePermission)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                    }
                },
                confirmButton = {}
            )
        }

        if (showTimePicker.value) {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    val formatted = "%02d:%02d".format(hour, minute)
                    viewModel.onNotificationTimeChange(formatted)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()

            showTimePicker.value = false
        }

        TextFields(
            username = state.username,
            post = state.job,
            resumeUrl = state.resumeUrl,
            notificationTime = state.notificationTime,
            notificationTimeError = state.notificationTimeError,
            onUsernameChange = viewModel::onUsernameTextChange,
            onPostChange = viewModel::onJobTextChange,
            onResumeUrlChange = viewModel::onResumeUrlChange,
            onNotificationTimeChange = viewModel::onNotificationTimeChange,
            onClockClick = { showTimePicker.value = true },
        )

        Button(
            onClick = {
                viewModel.updateUserInfo()
                onBack()
            },
            modifier = Modifier.padding(top = 20.dp),
        ) {
            Text("Сохранить изменения")
        }
    }
}

@Composable
private fun TextFields(
    username: String,
    post: String,
    resumeUrl: String,
    notificationTime: String,
    notificationTimeError: String? = null,
    onUsernameChange: (String) -> Unit,
    onPostChange: (String) -> Unit,
    onResumeUrlChange: (String) -> Unit,
    onNotificationTimeChange: (String) -> Unit,
    onClockClick: () -> Unit,
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Имя") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
    )

    OutlinedTextField(
        value = post,
        onValueChange = onPostChange,
        label = { Text("Работа") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    )

    OutlinedTextField(
        value = resumeUrl,
        onValueChange = onResumeUrlChange,
        label = { Text("Ссылка на ваше резюме") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    )

    OutlinedTextField(
        value = notificationTime,
        onValueChange = { onNotificationTimeChange(it) },
        label = { Text("Время выхода нового фильма") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.clickable { onClockClick() }
            )
        },
        isError = notificationTimeError != null,
        supportingText = { notificationTimeError?.let { Text(text = it, color = Color.Red) } },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    )
}

private fun saveBitmap(context: Context, bitmap: Bitmap): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "avatar_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Avatars")
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        resolver.openOutputStream(it)?.use { out: OutputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
    }

    return uri
}