package com.mmarra.movie.data.repository

import com.mmarra.movie.data.network.KinopoiskApi
import com.mmarra.movie.data.network.dto.MovieListResponse
import com.mmarra.movie.data.network.mappers.toDomain

import com.mmarra.movie.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkMovieRepository @Inject constructor(
    private val api: KinopoiskApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    override suspend fun getMovies(): List<Movie> = withContext(dispatcher) {
        val filters = mapOf(
            "limit" to "10",
            "page" to "1",
            "notNullFields" to "poster.url"
        )
        val response = api.getMovies(filters)
        response.docs.map { it.toDomain() }
    }

    override suspend fun getMovies(filters: Map<String, String>): List<Movie> = withContext(dispatcher) {
        val newFilters = filters.toMutableMap()
        newFilters["notNullFields"] = "rating.russianFilmCritics"
        val response = api.getMovies(newFilters)
        response.docs.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(id: Int): Movie? = withContext(dispatcher) {
        try {
            val response = api.getMovieById(id)
            response.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchMovies(query: String, filters: Map<String, String>): List<Movie> = withContext(dispatcher) {
        try {
            val searchParams = mutableMapOf<String, String>().apply {
                putAll(filters)
                if (!containsKey("limit")) put("limit", "10")
                if (!containsKey("page")) put("page", "1")
            }

            val response = api.searchMovies(query, searchParams)
            response.docs.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
