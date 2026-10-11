package com.example.ghiblicatalog.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Query("SELECT * FROM favorite_films ORDER BY title ASC")
    fun getAllFavorites(): Flow<List<FavoriteFilm>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE id = :filmId)")
    fun isFavorite(filmId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(film: FavoriteFilm)

    @Delete
    suspend fun removeFavorite(film: FavoriteFilm)
}