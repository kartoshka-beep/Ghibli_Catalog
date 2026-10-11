package com.example.ghiblicatalog.data

import com.example.ghiblicatalog.data.local.FavoriteFilm
import com.example.ghiblicatalog.data.local.FavoriteFilmDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteFilmDao: FavoriteFilmDao
) {

    fun getAllFavorites(): Flow<List<FavoriteFilm>> {
        return favoriteFilmDao.getAllFavorites()
    }

    fun isFavorite(filmId: String): Flow<Boolean> {
        return favoriteFilmDao.isFavorite(filmId)
    }

    suspend fun addFavorite(film: FavoriteFilm) {
        favoriteFilmDao.addFavorite(film)
    }

    suspend fun removeFavorite(film: FavoriteFilm) {
        favoriteFilmDao.removeFavorite(film)
    }
}