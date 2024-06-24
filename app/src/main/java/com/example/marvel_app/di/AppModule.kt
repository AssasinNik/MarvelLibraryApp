package com.example.marvel_app.di

import com.example.marvel_app.data.remote.MarvelApi
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHeroRepository(
        api: MarvelApi
    ) = HeroRepository(api)

    @Singleton
    @Provides
    fun provideHeroApi(): MarvelApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MarvelApi::class.java)
    }
}