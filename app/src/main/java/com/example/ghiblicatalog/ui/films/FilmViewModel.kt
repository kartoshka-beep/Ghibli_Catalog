package com.example.ghiblicatalog.ui.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghiblicatalog.data.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class FilmViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmUiState>(FilmUiState.Loading)
    val uiState: StateFlow<FilmUiState> = _uiState.asStateFlow()

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            _uiState.value = FilmUiState.Loading

            try {
                val films = repository.getFilms()

                _uiState.value = if (films.isEmpty()) {
                    FilmUiState.Empty
                } else {
                    FilmUiState.Success(films)
                }
            } catch (exception: Exception) {
                _uiState.value = FilmUiState.Error(
                    exception.message ?: "Не удалось загрузить фильмы"
                )
            }
        }
    }
}