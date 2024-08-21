package com.example.marvel_app.data.models

data class TvShowEntry(
    val id: Int,
    val title: String,
    val season: Int,
    val date: String,
    val director: String,
    val overview: String?,
    val imdb_id: String,
    val number_episodes: Int,
    val cover_url: String,
    val trailer_url: String?
)