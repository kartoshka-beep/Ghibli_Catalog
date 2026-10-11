package com.example.ghiblicatalog.data

import com.google.gson.annotations.SerializedName

data class Film(
    val id: String,
    val title: String,
    val description: String = "",
    val director: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    val image: String = ""
)