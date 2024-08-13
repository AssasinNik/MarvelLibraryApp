package com.example.marvel_app.data.local.comics_favourites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comics(
    val comicsName: String?= "",
    val imageUrl: String? = "",
    val description: String? = "",
    val number: Int? = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int?
)