package com.example.ghiblicatalog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films")
data class FavoriteFilm(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val director: String,
    val releaseDate: String,
    val image: String
)