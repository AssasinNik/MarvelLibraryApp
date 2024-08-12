package com.example.marvel_app.data.remote.responses.CharacterComics

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXX>,
    val returned: Int
)