package com.mmarra.domain.repository

import com.mmarra.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Movie>>
    fun observeIsFavorite(id: Int): Flow<Boolean>

    suspend fun addToFavorites(movie: Movie)
    suspend fun removeFromFavorites(id: Int)
    suspend fun getById(id: Int): Movie?
    suspend fun isFavorite(id: Int): Boolean

    suspend fun toggleFavorite(shortMovie: Movie): Boolean
}