package com.mmarra.movie.domain.repository

import com.mmarra.movie.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile>
    suspend fun updateProfile(profile: Profile)
}