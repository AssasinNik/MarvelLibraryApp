package com.example.marvel_app.data.models

import androidx.compose.runtime.Immutable


@Immutable
data class HeroesListEntry(
    val characterName: String,
    val imageUrl: String,
    val number :Int
)
