package com.mmarra.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mmarra.domain.model.Profile
import com.mmarra.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.profileDataStore by preferencesDataStore("profile")

@Singleton
class LocalProfileRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : ProfileRepository {
    companion object {
        private val KEY_USERNAME = stringPreferencesKey("username")
        private val KEY_JOB = stringPreferencesKey("job")
        private val KEY_PHOTO_URI = stringPreferencesKey("photo_uri")
        private val KEY_RESUME_URL = stringPreferencesKey("resume_url")
        private val KEY_NOTIFICATION_TIME = stringPreferencesKey("notification_time")
    }

    override fun observeProfile(): Flow<Profile> = context.profileDataStore.data.map { prefs ->
        Profile(
            username = prefs[KEY_USERNAME] ?: "",
            job = prefs[KEY_JOB] ?: "",
            photoUri = prefs[KEY_PHOTO_URI] ?: "",
            resumeUrl = prefs[KEY_RESUME_URL] ?: "",
            notificationTime = prefs[KEY_NOTIFICATION_TIME] ?: "",
        )
    }

    override suspend fun updateProfile(profile: Profile) {
        context.profileDataStore.edit { prefs ->
            prefs[KEY_USERNAME] = profile.username
            prefs[KEY_JOB] = profile.job
            prefs[KEY_PHOTO_URI] = profile.photoUri
            prefs[KEY_RESUME_URL] = profile.resumeUrl
            prefs[KEY_NOTIFICATION_TIME] = profile.notificationTime
        }
    }
}