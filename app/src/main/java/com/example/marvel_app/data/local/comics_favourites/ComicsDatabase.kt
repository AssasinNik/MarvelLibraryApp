package com.example.marvel_app.data.local.comics_favourites

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Comics::class],
    exportSchema = false,
    version = 7
)
abstract class ComicsDatabase: RoomDatabase(){
    abstract val dao: ComicsDao
}
