package com.example.marvel_app.data.remote.responses.Comics

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)