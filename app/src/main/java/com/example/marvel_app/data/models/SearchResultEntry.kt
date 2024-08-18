package com.example.marvel_app.data.models

import androidx.compose.runtime.Immutable

@Immutable
data class SearchResultEntry(
    val name: String?,
    val description: String?,
    val imageUrl: String?,
    val type: String?,
    val number: Int?
)