package com.example.marvel_app.data.models

import androidx.compose.runtime.Immutable

@Immutable
data class ComicsItemEntry(
    val comicsName : String?,
    val comicsDescription: String?,
    val comicsImage: String?,
    val number : Int?,
    val shareLink: String,
    val creators: List<Creators>? = listOf(),
    val characterEntry: List<CharacterEntry>? = listOf()
)

data class Creators(
    val name: String?,
    val role: String?
)