package com.example.marvel_app.data.local.favourites

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {

    @Upsert
    fun upsertFavourite(favourites: FavouritesEntity)

    @Query("DELETE FROM FavouritesEntity WHERE name = :name AND category = :category")
    fun deleteFavourite(name: String, category: String)

    @Query("SELECT * FROM FavouritesEntity")
    fun selectFavourites(): Flow<List<FavouritesEntity>>

    @Query("SELECT * FROM FavouritesEntity WHERE category = :category")
    fun selectFavouritesCategory(category: String): Flow<List<FavouritesEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM FavouritesEntity WHERE name = :name AND category = :category)")
    fun existsFavourites(name: String, category: String): Boolean
}