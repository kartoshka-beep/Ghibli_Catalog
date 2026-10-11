package com.example.ghiblicatalog.ui.films

import com.example.ghiblicatalog.data.Film

sealed interface FilmUiState {
    data object Loading : FilmUiState

    data class Success(
        val films: List<Film>
    ) : FilmUiState

    data object Empty : FilmUiState

    data class Error(
        val message: String
    ) : FilmUiState
}