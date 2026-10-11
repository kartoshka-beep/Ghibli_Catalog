package com.example.ghiblicatalog.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghiblicatalog.data.Film
import com.example.ghiblicatalog.data.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.ghiblicatalog.data.FavoriteRepository
import com.example.ghiblicatalog.data.toFavoriteFilm

@HiltViewModel
class FilmDetailViewModel @Inject constructor(
    private val repository: FilmRepository,
    private val favoriteRepository: FavoriteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val filmId: String = checkNotNull(savedStateHandle["filmId"])
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadFilm()

        viewModelScope.launch {
            favoriteRepository.isFavorite(filmId).collect { favorite ->
                _isFavorite.value = favorite
            }
        }
    }

    fun loadFilm() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _film.value = repository.getFilmById(filmId)
            } catch (exception: Exception) {
                _error.value = exception.message
                    ?: "Не удалось загрузить информацию о фильме"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun toggleFavorite() {
        viewModelScope.launch {
            val currentFilm = _film.value ?: return@launch

            if (_isFavorite.value) {
                favoriteRepository.removeFavorite(currentFilm.toFavoriteFilm())
            } else {
                favoriteRepository.addFavorite(currentFilm.toFavoriteFilm())
            }
        }
    }
}