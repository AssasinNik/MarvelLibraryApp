package com.example.marvel_app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.marvel_app.data.local.favourites.FavouriteDao
import com.example.marvel_app.data.local.favourites.FavouriteDatabase
import com.example.marvel_app.data.local.heroes.HeroesDao
import com.example.marvel_app.data.local.heroes.HeroesDatabase
import com.example.marvel_app.data.remote.MarvelApi
import com.example.marvel_app.data.remote.MarvelAuthenticationInterceptor
import com.example.marvel_app.data.remote.MarvelCinematicApi
import com.example.marvel_app.presentation.marvel_start_screen.GoogleAuthUiClient
import com.example.marvel_app.repository.CinemaRepository
import com.example.marvel_app.repository.HeroRepository
import com.example.marvel_app.util.Constants.BASE_CINEMATIC_URL
import com.example.marvel_app.util.Constants.BASE_URL
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHeroRepository(@Named("Marvel") api: MarvelApi): HeroRepository {
        return HeroRepository(api)
    }

    @Singleton
    @Provides
    fun provideCinemaRepository(@Named("MarvelCinematic") api: MarvelCinematicApi): CinemaRepository {
        return CinemaRepository(api)
    }

    @Singleton
    @Provides
    @Named("Marvel")
    fun provideMarvelApi(@Named("AuthClient") client: OkHttpClient): MarvelApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(MarvelApi::class.java)
    }

    @Singleton
    @Provides
    @Named("MarvelCinematic")
    fun provideMarvelCinameticApi(@Named("NoAuthClient") client: OkHttpClient): MarvelCinematicApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_CINEMATIC_URL)
            .build()
            .create(MarvelCinematicApi::class.java)
    }

    @Singleton
    @Provides
    @Named("AuthClient")
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
    @Named("NoAuthClient")
    fun provideOkHttpCinemaClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set the desired logging level
        }

        return OkHttpClient.Builder()
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
    fun provideComicsDatabase(app:Application): FavouriteDatabase {
        return Room.databaseBuilder(
            app,
            FavouriteDatabase::class.java,
            "favourites.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideComicsDao(db: FavouriteDatabase): FavouriteDao {
        return db.dao
    }

    @Provides
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

}
