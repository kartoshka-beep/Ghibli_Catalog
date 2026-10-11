package com.example.ghiblicatalog.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilmRepositoryTest {

    private lateinit var api: GhibliApi
    private lateinit var repository: FilmRepository

    @Before
    fun setUp() {
        api = mockk()
        repository = FilmRepository(api)
    }

    @Test
    fun `getFilmById returns film from API`() = runTest {
        val film = Film(
            id = "1",
            title = "My Neighbor Totoro"
        )

        coEvery {
            api.getFilmById("1")
        } returns FilmResponse(film)

        val result = repository.getFilmById("1")

        assertEquals(film, result)
    }
}