package com.mmarra.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mmarra.domain.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.notificationPrefs by preferencesDataStore("notification")

@Singleton
class LocalNotificationRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : NotificationRepository {

    companion object {
        private val KEY_PERMISSION = booleanPreferencesKey("permission_accepted")
    }

    override fun observePopup(): Flow<Boolean> = context.notificationPrefs.data.map { prefs ->
        prefs[KEY_PERMISSION] ?: false
    }

    override suspend fun allowNotifications() {
        context.notificationPrefs.edit { prefs -> prefs[KEY_PERMISSION] = true }
    }
}