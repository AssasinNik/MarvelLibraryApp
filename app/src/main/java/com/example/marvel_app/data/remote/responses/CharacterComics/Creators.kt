package com.example.marvel_app.data.remote.responses.CharacterComics

data class Creators(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemX>,
    val returned: Int
)