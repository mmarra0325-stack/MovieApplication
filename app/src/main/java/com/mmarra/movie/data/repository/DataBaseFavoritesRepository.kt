package com.mmarra.movie.data.repository

import com.mmarra.movie.domain.repository.FavoritesRepository
import com.mmarra.movie.data.database.FavoriteDao
import com.mmarra.movie.data.database.mappers.toFavoriteEntity
import com.mmarra.movie.data.database.mappers.toMovie
import com.mmarra.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBaseFavoritesRepository @Inject constructor(
    private val dao: FavoriteDao
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Movie>> {
        return dao.observeAll()
            .map { entities -> entities.map { it.toMovie() } }
    }

    override fun observeIsFavorite(id: Int): Flow<Boolean> {
        return dao.observeIsFavorite(id)
    }

    override suspend fun getFavoriteById(id: Int): Movie? {
        return dao.getById(id)?.toMovie()
    }

    override suspend fun addToFavorites(movie: Movie) {
        dao.upsert(movie.toFavoriteEntity())
    }

    override suspend fun removeFromFavorites(id: Int) {
        dao.deleteById(id)
    }
}