package com.example.marvel_app.data.local.heroes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroesDao {

    @Upsert
    fun insertHero(hero: Heroes)

    @Transaction
    @Delete
    fun deleteHero(hero: Heroes)

    @Transaction
    @Query("DELETE FROM heroes")
    fun deleteHeroes()

    @Query("SELECT * FROM heroes")
    fun selectHeroes(): Flow<List<Heroes>>
}