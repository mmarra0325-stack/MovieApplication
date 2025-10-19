package com.mmarra.movie.data.repository

import com.mmarra.movie.model.Movie

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovies(filters: Map<String, String>): List<Movie>
    suspend fun getMovieDetails(id: Int): Movie?
    suspend fun searchMovies(query: String, filters: Map<String, String> = emptyMap()): List<Movie>
}