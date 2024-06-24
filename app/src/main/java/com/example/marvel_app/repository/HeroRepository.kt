package com.example.marvel_app.repository

import com.example.marvel_app.data.remote.MarvelApi
import com.example.marvel_app.data.remote.responses.ListHeroes
import com.example.marvel_app.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class HeroRepository @Inject constructor(
    private val api: MarvelApi
){
    suspend fun getHeroList(limit: Int) : Resource<ListHeroes>{
        val response = try{
            api.getHeroList(limit)
        } catch (e: Exception){
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
    suspend fun getHeroInfo(heroId: Int) : Resource<ListHeroes>{
        val response = try{
            api.getHero(heroId)
        } catch (e: Exception){
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
}