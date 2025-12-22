package com.mmarra.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.domain.model.Profile
import com.mmarra.domain.repository.AlarmManager
import com.mmarra.domain.repository.ProfileRepository
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
        val timeError = if (time.isNotBlank() && !validateNotificationTime(time)) {
            "Некорректный формат времени"
        } else null

        _state.update {
            it.copy(
                notificationTime = time,
                notificationTimeError = timeError,
            )
        }
    }

    private fun validateNotificationTime(text: String): Boolean {
        if (text.length != 5) return false
        if (text[2] != ':') return false

        val hour = text.take(2).toIntOrNull() ?: return false
        val minute = text.substring(3, 5).toIntOrNull() ?: return false

        return hour in 0..23 && minute in 0..59
    }

    fun updateUserInfo(): Boolean {
        val state = _state.value

        if (state.notificationTime.isNotBlank()) {
            if (!validateNotificationTime(state.notificationTime)) {
                _state.update { it.copy(notificationTimeError = "Некорректный формат времени") }
                return false
            }
        }

        _state.update { it.copy(notificationTimeError = null) }

        viewModelScope.launch {
            val profile = Profile(
                username = _state.value.username,
                job = _state.value.job,
                photoUri = _state.value.photoUri,
                resumeUrl = _state.value.resumeUrl,
                notificationTime = state.notificationTime.ifBlank { "" },
            )

            if (profile.notificationTime.isNotBlank()) {
                alarmManager.schedule(profile.notificationTime)
            }
            repository.updateProfile(profile)
        }

        return true
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