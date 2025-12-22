package com.mmarra.domain.repository

interface AlarmManager {
    fun schedule(notificationTime: String)
}