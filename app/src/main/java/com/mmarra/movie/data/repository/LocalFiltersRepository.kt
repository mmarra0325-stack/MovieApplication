package com.mmarra.movie.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mmarra.movie.domain.model.MovieFilters
import com.mmarra.movie.domain.repository.FiltersRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.movieFiltersDataStore by preferencesDataStore(name = "movie_filters")

@Singleton
class LocalFiltersRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : FiltersRepository {

    private object Keys {
        val GENRE = stringPreferencesKey("genre")
        val YEAR = stringPreferencesKey("year")
        val RATING_FROM = stringPreferencesKey("rating")
    }

    override fun observeFilters(): Flow<MovieFilters> {
        return context.movieFiltersDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences())
                else throw e
            }
            .map { prefs ->

                val genreRaw = prefs[Keys.GENRE]
                val yearRaw = prefs[Keys.YEAR]
                val ratingRaw = prefs[Keys.RATING_FROM]

                MovieFilters(
                    genre = genreRaw?.takeIf { it.isNotBlank() } ?: "",
                    year = yearRaw?.toIntOrNull() ?: 2000,
                    rating = ratingRaw?.toFloatOrNull() ?: 0f,
                )
            }
    }

    override suspend fun setFilters(filters: MovieFilters) {
        context.movieFiltersDataStore.edit { prefs ->
            prefs[Keys.GENRE] = filters.genre
            prefs[Keys.YEAR] = filters.year.toString()
            prefs[Keys.RATING_FROM] = filters.rating.toString()
        }
    }

    override suspend fun clearFilters() {
        context.movieFiltersDataStore.edit { prefs ->
            prefs[Keys.GENRE] = ""
            prefs[Keys.YEAR] = ""
            prefs[Keys.RATING_FROM] = ""
        }
    }
}