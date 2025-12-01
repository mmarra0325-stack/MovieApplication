package com.mmarra.movie.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.domain.model.Profile
import com.mmarra.movie.domain.repository.ProfileRepository
import com.mmarra.movie.services.AlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val alarmManager: AlarmManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repository.observeProfile().collect { profile ->
                _state.value = ProfileUiState(
                    username = profile.username,
                    job = profile.job,
                    photoUri = profile.photoUri,
                    resumeUrl = profile.resumeUrl,
                    notificationTime = profile.notificationTime,
                )
            }
        }
    }

    fun onUsernameTextChange(username: String) = _state.update { it.copy(username = username) }
    fun onJobTextChange(job: String) = _state.update { it.copy(job = job) }
    fun onPhotoUriChange(photoUri: String) = _state.update { it.copy(photoUri = photoUri) }
    fun onResumeUrlChange(resumeUrl: String) = _state.update { it.copy(resumeUrl = resumeUrl) }

    fun onNotificationTimeChange(time: String) {
        _state.update { it.copy(notificationTime = time, notificationTimeError = null) }
    }

    private fun validateNotificationTime(text: String): Boolean {
        if (text.length != 5) return false
        if (text[2] != ':') return false

        val hour = text.take(2).toIntOrNull() ?: return false
        val minute = text.substring(3, 5).toIntOrNull() ?: return false

        return hour in 0..23 && minute in 0..59
    }

    fun updateUserInfo() {
        viewModelScope.launch {
            val profile = Profile(
                username = _state.value.username,
                job = _state.value.job,
                photoUri = _state.value.photoUri,
                resumeUrl = _state.value.resumeUrl,
                notificationTime = if (_state.value.notificationTime.isNotBlank()) {
                    if (!validateNotificationTime(_state.value.notificationTime)) {
                        _state.update { it.copy(notificationTimeError = "Некорректный формат времени") }
                        ""
                    } else {
                        _state.update { it.copy(notificationTimeError = null) }
                        _state.value.notificationTime
                    }
                } else {
                    ""
                },
            )

            if (profile.notificationTime.isNotBlank()) {
                alarmManager.schedule(profile.notificationTime)
            }
            repository.updateProfile(profile)
        }
    }
}

data class ProfileUiState(
    val username: String = "",
    val job: String = "",
    val photoUri: String = "",
    val resumeUrl: String = "",
    val notificationTime: String = "",
    val notificationTimeError: String? = null,
)