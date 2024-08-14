package com.example.marvel_app.data.remote.responses.TvShows

data class Data(
    val cover_url: String,
    val directed_by: String,
    val id: Int,
    val imdb_id: String,
    val last_aired_date: String,
    val number_episodes: Int,
    val overview: String,
    val phase: Int,
    val release_date: String,
    val saga: String,
    val season: Int,
    val title: String,
    val trailer_url: String
)