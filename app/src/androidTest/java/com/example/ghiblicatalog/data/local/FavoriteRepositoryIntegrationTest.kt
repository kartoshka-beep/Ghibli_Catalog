package com.example.ghiblicatalog.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ghiblicatalog.data.FavoriteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteRepositoryIntegrationTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: FavoriteRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        repository = FavoriteRepository(database.favoriteFilmDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addedFavorite_canBeReadThroughRepository() = runBlocking {
        val film = FavoriteFilm(
            id = "film-123",
            title = "My Neighbor Totoro",
            description = "A family meets forest spirits",
            director = "Hayao Miyazaki",
            releaseDate = "1988",
            image = ""
        )

        repository.addFavorite(film)

        val favorites = repository.getAllFavorites().first()

        assertEquals(listOf(film), favorites)
        assertTrue(repository.isFavorite("film-123").first())
    }

    @Test
    fun addingSameFavoriteTwice_doesNotCreateDuplicate() = runBlocking {
        val film = FavoriteFilm(
            id = "film-456",
            title = "Spirited Away",
            description = "A girl enters a spirit world",
            director = "Hayao Miyazaki",
            releaseDate = "2001",
            image = ""
        )

        repository.addFavorite(film)
        repository.addFavorite(film)

        val favorites = repository.getAllFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals(film, favorites.single())
    }
}
