package com.example.marvel_app.data.remote

import com.example.marvel_app.data.remote.responses.ListHeroes
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MarvelApi {
    @GET("characters")
    suspend fun getHeroList(
        @Query("limit") limit : Int,
        @Query("orderBy") order: String = "name"
    ): ListHeroes

    @GET("characters/{id}")
    suspend fun getHero(
        @Path("id") id : Int
    ): ListHeroes
}