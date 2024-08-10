package com.example.marvel_app.data.models

import androidx.compose.runtime.Immutable

@Immutable
data class ComicsEntry(
    val comicsName : String,
    val comicsDescription: String?,
    val comicsImage: String
)
