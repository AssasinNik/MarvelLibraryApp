package com.example.marvel_app.di

import android.app.Application
import androidx.room.Room
import com.example.marvel_app.data.local.comics_favourites.ComicsDao
import com.example.marvel_app.data.local.comics_favourites.ComicsDatabase
import com.example.marvel_app.data.local.heroes.HeroesDao
import com.example.marvel_app.data.local.heroes.HeroesDatabase
import com.example.marvel_app.data.remote.MarvelApi
import com.example.marvel_app.data.remote.MarvelAuthenticationInterceptor
import com.example.marvel_app.data.remote.MarvelCinematicApi
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.BASE_CINEMATIC_URL
import com.example.marvel_app.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideMarvelCinameticApi(): MarvelCinematicApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_CINEMATIC_URL)
            .build()
            .create(MarvelCinematicApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: MarvelAuthenticationInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set the desired logging level
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor) // Add the logging interceptor
            .connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideMarvelAuthenticationInterceptor(): MarvelAuthenticationInterceptor {
        return MarvelAuthenticationInterceptor()
    }

    @Provides
    @Singleton
    fun provideHeroesDatabase(app:Application): HeroesDatabase {
        return Room.databaseBuilder(
            app,
            HeroesDatabase::class.java,
            "heroes.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideHeroesDao(db: HeroesDatabase): HeroesDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideComicsDatabase(app:Application): ComicsDatabase {
        return Room.databaseBuilder(
            app,
            ComicsDatabase::class.java,
            "comics.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideComicsDao(db: ComicsDatabase): ComicsDao {
        return db.dao
    }

}
