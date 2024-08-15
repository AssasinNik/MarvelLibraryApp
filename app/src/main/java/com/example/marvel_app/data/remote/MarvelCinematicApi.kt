package com.example.marvel_app.data.remote

import com.example.marvel_app.data.remote.responses.Films.FilmsInfo
import com.example.marvel_app.data.remote.responses.TvShows.TvShows
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelCinematicApi {

    @GET("movies")
    suspend fun getMovies(
        @Query("limit") limit : Int
    ): FilmsInfo

    @GET("movies")
    suspend fun getMoviesByTitle(
        @Query("title") title : String
    ): FilmsInfo

    @GET("tvshows")
    suspend fun getTvShows(
        @Query("limit") limit : Int
    ): TvShows

    @GET("tvShows")
    suspend fun getTvShowsByTitle(
        @Query("title") title : String
    ): TvShows

}