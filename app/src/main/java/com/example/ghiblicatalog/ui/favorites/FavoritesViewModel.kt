package com.example.ghiblicatalog.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghiblicatalog.data.FavoriteRepository
import com.example.ghiblicatalog.data.local.FavoriteFilm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoriteRepository: FavoriteRepository
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteFilm>> =
        favoriteRepository.getAllFavorites().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}