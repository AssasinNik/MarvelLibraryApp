package com.example.marvel_app.data.local.favourites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouritesEntity(
    val name: String?= "",
    val imageUrl: String? = "",
    val description: String? = "",
    val number: Int? = 0,
    val category: String? = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int?
)