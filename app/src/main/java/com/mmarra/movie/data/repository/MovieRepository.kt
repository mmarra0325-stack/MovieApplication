package com.mmarra.movie.data.repository

import com.mmarra.movie.model.Movie

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieDetails(id: Int): Movie?
}