package com.example.marvel_app.data.local.favourites

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ComicsDao {

    @Upsert
    fun upsertComics(comics: Comics)

    @Query("DELETE FROM comics WHERE comicsName = :comicsName")
    fun deleteComics(comicsName: String)

    @Query("SELECT * FROM comics")
    fun selectHeroes(): Flow<List<Comics>>

    @Query("SELECT EXISTS(SELECT 1 FROM comics WHERE comicsName = :comicsName)")
    fun existsComics(comicsName: String): Boolean
}