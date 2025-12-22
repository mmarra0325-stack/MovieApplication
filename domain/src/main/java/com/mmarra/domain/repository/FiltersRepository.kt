package com.mmarra.domain.repository

import com.mmarra.domain.model.MovieFilters
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    fun observeFilters(): Flow<MovieFilters>
    suspend fun setFilters(filters: MovieFilters)
    suspend fun clearFilters()
}