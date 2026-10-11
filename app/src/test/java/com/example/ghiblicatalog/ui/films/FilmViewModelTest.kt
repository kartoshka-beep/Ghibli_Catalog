package com.example.ghiblicatalog.ui.films

import com.example.ghiblicatalog.data.Film
import com.example.ghiblicatalog.data.FilmRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import io.mockk.coVerify
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher


@OptIn(ExperimentalCoroutinesApi::class)
class FilmViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FilmRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful load shows films`() = runTest(testDispatcher) {
        val films = listOf(
            Film(
                id = "1",
                title = "My Neighbor Totoro"
            )
        )

        coEvery { repository.getFilms() } returns films

        val viewModel = FilmViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            FilmUiState.Success(films),
            viewModel.uiState.value
        )
    }
    @Test
    fun `initial state is Loading`() = runTest(testDispatcher) {
        coEvery { repository.getFilms() } returns emptyList()

        val viewModel = FilmViewModel(repository)

        assertEquals(
            FilmUiState.Loading,
            viewModel.uiState.value
        )

        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `empty response shows Empty`() = runTest(testDispatcher) {
        coEvery { repository.getFilms() } returns emptyList()

        val viewModel = FilmViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            FilmUiState.Empty,
            viewModel.uiState.value
        )
    }

    @Test
    fun `failed load shows Error`() = runTest(testDispatcher) {
        coEvery { repository.getFilms() } throws RuntimeException("Network error")

        val viewModel = FilmViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            FilmUiState.Error("Network error"),
            viewModel.uiState.value
        )
    }
    @Test
    fun `retry starts loading films again`() = runTest(testDispatcher) {
        val films = listOf(Film(id = "1", title = "My Neighbor Totoro"))
        var calls = 0

        coEvery { repository.getFilms() } coAnswers {
            calls++
            if (calls == 1) {
                throw RuntimeException("Network error")
            } else {
                films
            }
        }

        val viewModel = FilmViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            FilmUiState.Error("Network error"),
            viewModel.uiState.value
        )

        viewModel.loadFilms()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(FilmUiState.Success(films), viewModel.uiState.value)
        coVerify(exactly = 2) { repository.getFilms() }
    }

    @Test
    fun `state emits Loading then Success`() = runTest(testDispatcher) {
        val films = listOf(Film(id = "1", title = "My Neighbor Totoro"))
        coEvery { repository.getFilms() } returns films

        val viewModel = FilmViewModel(repository)
        val emissions = mutableListOf<FilmUiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.take(2).toList(emissions)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            listOf(
                FilmUiState.Loading,
                FilmUiState.Success(films)
            ),
            emissions
        )
    }

    @Test
    fun `state emits Error then Loading then Success on retry`() =
        runTest(testDispatcher) {
            val films = listOf(Film(id = "1", title = "My Neighbor Totoro"))
            var calls = 0

            coEvery { repository.getFilms() } coAnswers {
                calls++
                if (calls == 1) {
                    throw RuntimeException("Network error")
                } else {
                    films
                }
            }

            val viewModel = FilmViewModel(repository)
            val emissions = mutableListOf<FilmUiState>()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.take(4).toList(emissions)
            }

            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.loadFilms()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(
                listOf(
                    FilmUiState.Loading,
                    FilmUiState.Error("Network error"),
                    FilmUiState.Loading,
                    FilmUiState.Success(films)
                ),
                emissions
            )
            val newSubscriberEmissions = mutableListOf<FilmUiState>()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.take(1).toList(newSubscriberEmissions)
            }

            assertEquals(
                listOf(FilmUiState.Success(films)),
                newSubscriberEmissions
            )
        }
}