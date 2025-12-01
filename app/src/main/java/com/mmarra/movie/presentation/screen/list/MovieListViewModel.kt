package com.mmarra.movie.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.domain.model.Movie
import com.mmarra.movie.domain.model.MovieFilters
import com.mmarra.movie.domain.repository.FavoritesRepository
import com.mmarra.movie.domain.repository.FiltersRepository
import com.mmarra.movie.domain.repository.MovieRepository
import com.mmarra.movie.domain.repository.NotificationRepository
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
    private val filtersRepository: FiltersRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        observeNotifications()
        observeFavorites()
        observeFilters()
        loadMovies()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            notificationRepository.observePopup().collect { wasAccepted ->
                _uiState.update { it.copy(showPermissionDialog = !wasAccepted) }
            }
        }
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

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(movie)
        }
    }

    fun updateSearchQuery(value: String) {
        _uiState.update { it.copy(searchQuery = value) }
    }

    fun searchMovies() {
        viewModelScope.launch {
            try {
                val query = uiState.value.searchQuery

                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Loading,
                        currentPage = 1
                    )
                }

                val filters = buildFilters(
                    page = 1,
                    query = if (query.isBlank()) null else query,
                    movieFilters = uiState.value.filters
                )

                val movies = if (query.isNotBlank()) {
                    movieRepository.searchMovies(query, filters)
                } else {
                    movieRepository.getMovies(filters)
                }

                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Success(movies),
                        currentPage = 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(moviesState = MovieListMoviesState.Error(e.message ?: "Search error"))
                }
            }
        }
    }

    private fun observeFilters() {
        viewModelScope.launch {
            filtersRepository.observeFilters().collect { newFilters ->
                _uiState.update { it.copy(filters = newFilters, currentPage = 1) }
                reloadMoviesWithFilters()
            }
        }
    }

    private fun reloadMoviesWithFilters() {
        viewModelScope.launch {
            try {
                val query = uiState.value.searchQuery

                _uiState.update { it.copy(moviesState = MovieListMoviesState.Loading) }

                val filters = buildFilters(
                    page = 1,
                    query = if (query.isBlank()) null else query,
                    movieFilters = uiState.value.filters
                )

                val movies = if (query.isNotBlank()) {
                    movieRepository.searchMovies(query, filters)
                } else {
                    movieRepository.getMovies(filters)
                }

                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Success(movies),
                        currentPage = 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(moviesState = MovieListMoviesState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(moviesState = MovieListMoviesState.Loading) }

                val filters = buildFilters(
                    page = 1,
                    query = null,
                    movieFilters = uiState.value.filters
                )

                val movies = movieRepository.getMovies(filters)

                _uiState.update {
                    it.copy(
                        moviesState = MovieListMoviesState.Success(movies),
                        currentPage = 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(moviesState = MovieListMoviesState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingNextPage) return

        _uiState.update { it.copy(isLoadingNextPage = true) }

        viewModelScope.launch {
            try {
                val nextPage = state.currentPage + 1
                val query = state.searchQuery

                val filters = buildFilters(
                    page = nextPage,
                    query = if (query.isBlank()) null else query,
                    movieFilters = state.filters
                )

                val nextMovies = if (query.isNotBlank()) {
                    movieRepository.searchMovies(query, filters)
                } else {
                    movieRepository.getMovies(filters)
                }

                val currentMovies =
                    (uiState.value.moviesState as? MovieListMoviesState.Success)?.movies
                        ?: emptyList()

                _uiState.update {
                    it.copy(
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

    private fun buildFilters(
        page: Int,
        query: String?,
        movieFilters: MovieFilters
    ): Map<String, String> {

        val map = mutableMapOf(
            "page" to page.toString(),
            "limit" to "10"
        )

        if (movieFilters.genre.isNotBlank()) {
            map["genres.name"] = movieFilters.genre
        }

        movieFilters.year?.let { map["year"] = it.toString() }

        movieFilters.rating?.let { map["rating.kp"] = it.toString() }

        return map
    }

    fun onPermissionGranted() {
        viewModelScope.launch {
            notificationRepository.allowNotifications()
            _uiState.update { it.copy(showPermissionDialog = false) }
        }
    }

    fun onSkipPermission() {
        _uiState.update { it.copy(showPermissionDialog = false) }
    }
}

data class MovieListUiState(
    val moviesState: MovieListMoviesState = MovieListMoviesState.Loading,
    val showPermissionDialog: Boolean = false,
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val isLoadingNextPage: Boolean = false,
    val favoriteIds: Set<Int> = emptySet(),
    val filters: MovieFilters = MovieFilters(),
)

sealed class MovieListMoviesState {
    data class Success(val movies: List<Movie>) : MovieListMoviesState()
    object Loading : MovieListMoviesState()
    data class Error(val message: String) : MovieListMoviesState()
}