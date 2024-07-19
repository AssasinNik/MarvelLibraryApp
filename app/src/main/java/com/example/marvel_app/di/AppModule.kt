package com.example.marvel_app.di

import com.example.marvel_app.data.remote.MarvelApi
import com.example.marvel_app.data.remote.MarvelAuthenticationInterceptor
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideHeroRepository(api: MarvelApi): HeroRepository {
        return HeroRepository(api)
    }

    @Singleton
    @Provides
    fun provideMarvelApi(client: OkHttpClient): MarvelApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(MarvelApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: MarvelAuthenticationInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set the desired logging level
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the logging interceptor
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMarvelAuthenticationInterceptor(): MarvelAuthenticationInterceptor {
        return MarvelAuthenticationInterceptor()
    }
}
