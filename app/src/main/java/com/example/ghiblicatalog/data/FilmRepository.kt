package com.example.ghiblicatalog.data

class FilmRepository(private val api: GhibliApi) {
    suspend fun getFilms(): List<Film> {
        return api.getFilms().data
    }

    suspend fun getFilmById(id: String): Film {
        return api.getFilmById(id).data
    }
}