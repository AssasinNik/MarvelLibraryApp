package com.example.marvel_app.data.local.heroes

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Heroes::class],
    exportSchema = false,
    version = 7
)
abstract class HeroesDatabase: RoomDatabase() {
    abstract val dao: HeroesDao
}