package com.mmarra.movie.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.data.repository.MovieRepository
import com.mmarra.movie.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    fun loadNextPage() {
        if (_uiState.value.isLoadingNextPage) return

        _uiState.update { it.copy(isLoadingNextPage = true) }

        viewModelScope.launch {
            try {
                val nextPage = _uiState.value.currentPage + 1
                val filters = mapOf("page" to nextPage.toString(), "limit" to "10")

                val nextMovies = if (_uiState.value.searchQuery != null) {
                    repository.searchMovies(_uiState.value.searchQuery!!, filters)
                } else {
                    repository.getMovies(filters)
                }

                _uiState.update { state ->
                    val currentMovies = (state.moviesState as? MovieListMoviesState.Success)?.movies ?: emptyList()
                    state.copy(
                        moviesState = MovieListMoviesState.Success(currentMovies + nextMovies),
                        currentPage = nextPage,
                        isLoadingNextPage = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingNextPage = false) }
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Loading,
                        searchQuery = if (query.isBlank()) null else query
                    )
                }

                val movies = if (query.isBlank()) {
                    repository.getMovies()
                } else {
                    repository.searchMovies(query)
                }

                _uiState.update { state ->
                    state.copy(
                        moviesState = MovieListMoviesState.Success(movies),
                        currentPage = 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Error(e.message ?: "Search error")
                    )
                }
            }
        }
    }

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(moviesState = MovieListMoviesState.Loading) }
                val movies = repository.getMovies()
                _uiState.update {
                    it.copy(moviesState = MovieListMoviesState.Success(movies))
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }
}

data class MovieListUiState(
    val moviesState: MovieListMoviesState = MovieListMoviesState.Loading,
    val searchQuery: String? = null,
    val currentPage: Int = 1,
    val isLoadingNextPage: Boolean = false
)

sealed class MovieListMoviesState {
    data class Success(val movies: List<Movie>) : MovieListMoviesState()
    object Loading : MovieListMoviesState()
    data class Error(val message: String) : MovieListMoviesState()
}