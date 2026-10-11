package com.example.ghiblicatalog.di

import com.example.ghiblicatalog.data.FilmRepository
import com.example.ghiblicatalog.data.GhibliApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGhibliApi(): GhibliApi {
        return Retrofit.Builder()
            .baseUrl("https://ghibli-api.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GhibliApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFilmRepository(
        api: GhibliApi
    ): FilmRepository {
        return FilmRepository(api)
    }
}