package com.mmarra.movie.presentation.screen.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmarra.movie.domain.model.MovieFilters
import com.mmarra.movie.domain.repository.FiltersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val repository: FiltersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()

    init {
        loadFilters()
    }

    fun loadFilters() {
        viewModelScope.launch {
            repository.observeFilters().collect { filters ->
                _uiState.update {
                    it.copy(
                        genre = filters.genre,
                        year = filters.year?.toString() ?: "",
                        rating = filters.rating?.toString() ?: ""
                    )
                }
            }
        }
    }

    fun updateGenre(value: String) {
        _uiState.update { it.copy(genre = value) }
    }

    fun updateYear(value: String) {
        _uiState.update { it.copy(year = value) }
    }

    fun updateRating(value: String) {
        _uiState.update { it.copy(rating = value) }
    }

    fun saveFilters() {
        viewModelScope.launch {
            val filters = MovieFilters(
                genre = uiState.value.genre,
                year = uiState.value.year.toIntOrNull(),
                rating = uiState.value.rating.toFloatOrNull()
            )

            repository.setFilters(filters)
        }
    }

    fun clearFilters() {
        viewModelScope.launch {
            repository.clearFilters()
        }
    }
}

data class FiltersUiState(
    val genre: String = "",
    val year: String = "",
    val rating: String = "",
)

