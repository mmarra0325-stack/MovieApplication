package com.mmarra.movie.domain.repository

import com.mmarra.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun observeFavorites(): Flow<List<Movie>>

    fun observeIsFavorite(id: Int): Flow<Boolean>

    suspend fun getFavoriteById(id: Int): Movie?

    suspend fun addToFavorites(movie: Movie)

    suspend fun removeFromFavorites(id: Int)
}