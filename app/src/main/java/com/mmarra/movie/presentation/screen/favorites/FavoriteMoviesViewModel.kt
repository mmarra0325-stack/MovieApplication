package com.mmarra.movie.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.domain.model.Movie
import com.mmarra.movie.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
}

data class FavoriteMoviesUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false
) {
    val emptyMoviesList: Boolean
        get() = !isLoading && movies.isEmpty()
}