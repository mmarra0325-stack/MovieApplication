package com.mmarra.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.domain.model.Movie
import com.mmarra.domain.repository.FavoritesRepository
import com.mmarra.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            val localMovie = favoritesRepository.getById(movieId)
            if (localMovie != null) {
                _uiState.value = MovieDetailUiState.Success(localMovie)
            } else {
                _uiState.value = MovieDetailUiState.Loading
            }

            if (localMovie == null) {
                try {
                    val remoteMovie = movieRepository.getMovieDetails(movieId)
                    if (remoteMovie != null) {
                        _uiState.value = MovieDetailUiState.Success(remoteMovie)
                    } else {
                        _uiState.value = MovieDetailUiState.Error("Movie not found")
                    }
                } catch (e: Exception) {
                    _uiState.value = MovieDetailUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class MovieDetailUiState {
    data class Success(val movie: Movie) : MovieDetailUiState()
    object Loading : MovieDetailUiState()
    data class Error(val message: String) : MovieDetailUiState()
}