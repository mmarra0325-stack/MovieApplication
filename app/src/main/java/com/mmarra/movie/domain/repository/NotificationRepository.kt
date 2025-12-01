package com.mmarra.movie.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observePopup(): Flow<Boolean>
    suspend fun allowNotifications()
}