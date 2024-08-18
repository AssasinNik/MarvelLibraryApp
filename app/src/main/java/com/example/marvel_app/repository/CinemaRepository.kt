package com.example.marvel_app.repository

import com.example.marvel_app.data.remote.MarvelCinematicApi
import com.example.marvel_app.data.remote.responses.Films.FilmsInfo
import com.example.marvel_app.data.remote.responses.TvShows.TvShows
import com.example.marvel_app.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class CinemaRepository @Inject constructor(
    private val api: MarvelCinematicApi
) {
    suspend fun getMovies(limit: Int, title: String): Resource<FilmsInfo>{
        val response = try {
            api.getMovies(limit,"title=$title")
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
    suspend fun getMoviesByTitle(title: String): Resource<FilmsInfo>{
        val response = try {
            api.getMoviesByTitle("title=$title")
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
    suspend fun getTvShows(limit: Int, title: String): Resource<TvShows>{
        val response = try {
            api.getTvShows(limit,"title=$title")
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
    suspend fun getTvShowsByTitle(title: String): Resource<TvShows>{
        val response = try {
            api.getTvShowsByTitle("title=$title")
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
}