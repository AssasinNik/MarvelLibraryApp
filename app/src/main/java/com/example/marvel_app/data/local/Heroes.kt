package com.example.marvel_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Heroes(
    val characterName: String,
    val imageUrl: String,
    val number: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
