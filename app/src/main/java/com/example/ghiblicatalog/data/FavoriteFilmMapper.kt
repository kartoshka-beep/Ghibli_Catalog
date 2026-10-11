package com.example.ghiblicatalog.data

import com.example.ghiblicatalog.data.local.FavoriteFilm

fun Film.toFavoriteFilm(): FavoriteFilm {
    return FavoriteFilm(
        id = id,
        title = title,
        description = description,
        director = director,
        releaseDate = releaseDate,
        image = image
    )
}