package com.mmarra.domain.repository

import com.mmarra.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile>
    suspend fun updateProfile(profile: Profile)
}