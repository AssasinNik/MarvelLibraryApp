package com.example.marvel_app.remote.responses

import com.example.marvel_app.data.remote.responses.Item

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)