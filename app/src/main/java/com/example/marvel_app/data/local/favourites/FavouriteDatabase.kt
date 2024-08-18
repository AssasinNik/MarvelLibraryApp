package com.example.marvel_app.data.local.favourites

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavouritesEntity::class],
    exportSchema = false,
    version = 7
)
abstract class FavouriteDatabase: RoomDatabase(){
    abstract val dao: FavouriteDao
}
