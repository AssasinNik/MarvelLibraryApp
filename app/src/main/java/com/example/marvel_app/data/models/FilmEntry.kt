package com.example.marvel_app.data.models

data class FilmEntry (
    val id: Int,
    val title: String,
    val coverUrl: String,
    val description: String?,
    val trailerUrl: String?,
    val duration: Int,
    val directedBy: String,
    val shareLink: String,
    val releaseDate: String
)