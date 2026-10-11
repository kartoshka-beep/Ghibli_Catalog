package com.example.ghiblicatalog.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

data class FilmsResponse(
    @SerializedName("data")
    val data: List<Film>
)

data class FilmResponse(
    @SerializedName("data")
    val data: Film
)

interface GhibliApi {
    @GET("films")
    suspend fun getFilms(): FilmsResponse

    @GET("films/{id}")
    suspend fun getFilmById(@Path("id") id: String): FilmResponse
}