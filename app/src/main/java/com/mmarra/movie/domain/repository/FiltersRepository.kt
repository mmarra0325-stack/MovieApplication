package com.mmarra.movie.domain.repository

import com.mmarra.movie.domain.model.MovieFilters
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    fun observeFilters(): Flow<MovieFilters>
    suspend fun setFilters(filters: MovieFilters)
    suspend fun clearFilters()
}