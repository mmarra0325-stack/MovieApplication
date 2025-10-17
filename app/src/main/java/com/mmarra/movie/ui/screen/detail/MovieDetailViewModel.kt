package com.mmarra.movie.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.data.repository.MovieRepository
import com.mmarra.movie.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(movieId)
                if (movie != null) {
                    _uiState.value = MovieDetailUiState.Success(movie)
                } else {
                    _uiState.value = MovieDetailUiState.Error("Movie not found")
                }
            } catch (e: Exception) {
                _uiState.value = MovieDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class MovieDetailUiState {
    data class Success(val movie: Movie) : MovieDetailUiState()
    object Loading : MovieDetailUiState()
    data class Error(val message: String) : MovieDetailUiState()
}