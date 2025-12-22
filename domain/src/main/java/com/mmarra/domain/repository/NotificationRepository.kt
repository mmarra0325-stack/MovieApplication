package com.mmarra.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observePopup(): Flow<Boolean>
    suspend fun allowNotifications()
}