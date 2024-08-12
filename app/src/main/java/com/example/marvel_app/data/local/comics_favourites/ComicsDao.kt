package com.example.marvel_app.data.local.comics_favourites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ComicsDao {
    @Upsert
    fun upsertComics(comics: Comics)

    @Delete
    fun deleteComics(comics: Comics)

    @Query("SELECT * FROM comics")
    fun selectHeroes(): Flow<List<Comics>>
}