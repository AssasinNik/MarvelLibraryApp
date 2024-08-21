package com.example.marvel_app.data.models

import androidx.compose.runtime.Immutable

@Immutable
data class CharacterEntry(
    val characterName: String,
    val imageUrl: String,
    val description : String,
    val shareLink: String,
    val number: Int
)
