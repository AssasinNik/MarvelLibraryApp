package com.example.marvel_app.data.remote.responses.Character

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXX>,
    val returned: Int
)