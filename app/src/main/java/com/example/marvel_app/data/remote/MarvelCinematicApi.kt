package com.example.marvel_app.data.remote

import com.example.marvel_app.data.remote.responses.Films.FilmsInfo
import com.example.marvel_app.data.remote.responses.TvShows.TvShows
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelCinematicApi {

    @GET("movies")
    suspend fun getMovies(
        @Query("limit") limit : Int,
        @Query("filter") title : String,
    ): FilmsInfo

    @GET("movies")
    suspend fun getMoviesByTitle(
        @Query("filter") title : String
    ): FilmsInfo

    @GET("tvshows")
    suspend fun getTvShows(
        @Query("limit") limit : Int,
        @Query("filter") title : String
    ): TvShows

    @GET("tvShows")
    suspend fun getTvShowsByTitle(
        @Query("filter") title : String
    ): TvShows

}