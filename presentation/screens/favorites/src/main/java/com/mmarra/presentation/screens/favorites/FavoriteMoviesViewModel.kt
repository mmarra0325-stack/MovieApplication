package com.mmarra.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.domain.model.Movie
import com.mmarra.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteMoviesUiState())
    val uiState: StateFlow<FavoriteMoviesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesRepository.observeFavorites()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .collect { movies ->
                    _uiState.update {
                        it.copy(
                            movies = movies,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(movieId)
        }
    }
}

data class FavoriteMoviesUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false
) {
    val emptyMoviesList: Boolean
        get() = !isLoading && movies.isEmpty()
}