package com.mmarra.movie.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.domain.model.Movie
import com.mmarra.movie.domain.repository.FavoritesRepository
import com.mmarra.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
        loadMovies()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesRepository.observeFavorites()
                .map { movies -> movies.map { it.id }.toSet() }
                .collect { favoriteIds ->
                    _uiState.update { it.copy(favoriteIds = favoriteIds) }
                }
        }
    }

    fun toggleFavorite(shortMovie: Movie) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(shortMovie)
        }
    }

    fun loadNextPage() {
        if (_uiState.value.isLoadingNextPage) return

        _uiState.update { it.copy(isLoadingNextPage = true) }

        viewModelScope.launch {
            try {
                val nextPage = _uiState.value.currentPage + 1
                val filters = mapOf("page" to nextPage.toString(), "limit" to "10")

                val nextMovies = if (_uiState.value.searchQuery != null) {
                    movieRepository.searchMovies(_uiState.value.searchQuery!!, filters)
                } else {
                    movieRepository.getMovies(filters)
                }

                _uiState.update { state ->
                    val currentMovies =
                        (state.moviesState as? MovieListMoviesState.Success)?.movies ?: emptyList()

                    state.copy(
                        moviesState = MovieListMoviesState.Success(currentMovies + nextMovies),
                        currentPage = nextPage,
                        isLoadingNextPage = false
                    )
                }
            } catch (_: Exception) {
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
                        searchQuery = query.ifBlank { null },
                    )
                }

                val movies = if (query.isBlank()) {
                    movieRepository.getMovies()
                } else {
                    movieRepository.searchMovies(query)
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

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(moviesState = MovieListMoviesState.Loading) }
                val movies = movieRepository.getMovies()
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
    val isLoadingNextPage: Boolean = false,
    val favoriteIds: Set<Int> = emptySet()
)

sealed class MovieListMoviesState {
    data class Success(val movies: List<Movie>) : MovieListMoviesState()
    object Loading : MovieListMoviesState()
    data class Error(val message: String) : MovieListMoviesState()
}