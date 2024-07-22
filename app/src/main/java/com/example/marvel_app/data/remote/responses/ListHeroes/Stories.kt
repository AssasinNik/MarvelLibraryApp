package com.example.marvel_app.data.remote.responses.ListHeroes

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXXX>,
    val returned: Int
)