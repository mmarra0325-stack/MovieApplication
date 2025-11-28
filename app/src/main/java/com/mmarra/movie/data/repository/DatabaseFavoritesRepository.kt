package com.mmarra.movie.data.repository

import com.mmarra.movie.data.database.FavoriteDao
import com.mmarra.movie.data.database.mappers.toFavoriteEntity
import com.mmarra.movie.data.database.mappers.toMovie
import com.mmarra.movie.domain.model.Movie
import com.mmarra.movie.domain.repository.FavoritesRepository
import com.mmarra.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseFavoritesRepository @Inject constructor(
    private val dao: FavoriteDao,
    private val movieRepository: MovieRepository,
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Movie>> =
        dao.observeAll().map { list -> list.map { it.toMovie() } }

    override fun observeIsFavorite(id: Int): Flow<Boolean> =
        dao.observeIsFavorite(id)

    override suspend fun addToFavorites(movie: Movie) {
        dao.upsert(movie.toFavoriteEntity())
    }

    override suspend fun removeFromFavorites(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun getById(id: Int): Movie? =
        dao.getById(id)?.toMovie()

    override suspend fun isFavorite(id: Int): Boolean =
        dao.getById(id) != null

    override suspend fun toggleFavorite(shortMovie: Movie): Boolean {
        val movieId = shortMovie.id
        val exists = isFavorite(movieId)

        return if (exists) {
            removeFromFavorites(movieId)
            false
        } else {
            // пробуем загрузить полную версию
            val detailMovie = try {
                movieRepository.getMovieDetails(movieId)
            } catch (e: Exception) {
                null
            }

            val movieToSave = detailMovie ?: shortMovie

            addToFavorites(movieToSave)

            detailMovie != null   // true = сохранили полный фильм
        }
    }
}