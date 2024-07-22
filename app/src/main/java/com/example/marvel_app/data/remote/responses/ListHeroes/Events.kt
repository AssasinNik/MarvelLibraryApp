package com.example.marvel_app.data.remote.responses.ListHeroes

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)