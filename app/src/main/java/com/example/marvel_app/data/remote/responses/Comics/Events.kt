package com.example.marvel_app.data.remote.responses.Comics

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)