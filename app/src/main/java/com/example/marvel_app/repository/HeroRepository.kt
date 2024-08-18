package com.example.marvel_app.repository

import com.example.marvel_app.data.remote.MarvelApi
import com.example.marvel_app.data.remote.responses.Character.Character
import com.example.marvel_app.data.remote.responses.CharacterComics.Comics
import com.example.marvel_app.data.remote.responses.Comics.ComicsInfo
import com.example.marvel_app.data.remote.responses.ListHeroes.ListHeroes
import com.example.marvel_app.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HeroRepository @Inject constructor(
    private val api: MarvelApi
) {
    suspend fun getHeroList(name: String): Resource<ListHeroes> {
        val response = try {
            api.getHeroList(name)
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }

    suspend fun getComicsList(name: String, limit: Int): Resource<Comics> {
        val response = try {
            api.getComicsList(name, limit)
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }

    suspend fun getHeroListLimit(name: String, limit: Int): Resource<ListHeroes> {
        val response = try {
            api.getHeroListLimit(name, limit)
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }

    suspend fun getHeroInfo(heroId: Int?): Resource<Character> {
        val response = try {
            api.getHero(heroId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
    suspend fun getHeroComics(heroId: Int?, limit: Int): Resource<Comics> {
        val response = try {
            api.getHeroComics(heroId, limit)
        } catch (e: Exception) {
            return Resource.Error("An unknown error")
        }
        return Resource.Success(response)
    }
    suspend fun getComics(comicsId: Int?): Resource<ComicsInfo>{
        val response = try {
            api.getComics(comicsId)
        } catch (e: Exception){
            return Resource.Error("An uknown error")
        }
        return Resource.Success(response)
    }
}
