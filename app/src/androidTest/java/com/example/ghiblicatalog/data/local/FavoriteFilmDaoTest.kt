package com.example.ghiblicatalog.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteFilmDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: FavoriteFilmDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.favoriteFilmDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savingFavorite_canBeReadBackFromDatabase() = runBlocking {
        val film = FavoriteFilm(
            id = "film-1",
            title = "My Neighbor Totoro",
            description = "A family meets forest spirits",
            director = "Hayao Miyazaki",
            releaseDate = "1988",
            image = "totoro.jpg"
        )

        dao.addFavorite(film)

        val savedFilms = dao.getAllFavorites().first()

        assertEquals(listOf(film), savedFilms)
        assertTrue(dao.isFavorite("film-1").first())
    }

    @Test
    fun removingFavorite_deletesItFromDatabase() = runBlocking {
        val film = FavoriteFilm(
            id = "film-2",
            title = "Spirited Away",
            description = "A girl enters a spirit world",
            director = "Hayao Miyazaki",
            releaseDate = "2001",
            image = "spirited-away.jpg"
        )

        dao.addFavorite(film)
        dao.removeFavorite(film)

        assertTrue(dao.getAllFavorites().first().isEmpty())
        assertFalse(dao.isFavorite("film-2").first())
    }
}