package com.example.ghiblicatalog.di

import android.content.Context
import androidx.room.Room
import com.example.ghiblicatalog.data.local.AppDatabase
import com.example.ghiblicatalog.data.local.FavoriteFilmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ghibli_catalog_database"
        ).build()
    }

    @Provides
    fun provideFavoriteFilmDao(
        database: AppDatabase
    ): FavoriteFilmDao {
        return database.favoriteFilmDao()
    }
}