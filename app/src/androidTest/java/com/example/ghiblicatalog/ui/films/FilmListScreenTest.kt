package com.example.ghiblicatalog.ui.films

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.ghiblicatalog.data.Film
import org.junit.Rule
import org.junit.Test
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class FilmListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun clickingFilm_navigatesToDetailScreenWithCorrectId() {
        val film = Film(
            id = "film-123",
            title = "My Neighbor Totoro",
            description = "A family meets forest spirits",
            director = "Hayao Miyazaki",
            releaseDate = "1988",
            image = ""
        )

        composeRule.setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "films"
            ) {
                composable("films") {
                    FilmListScreen(
                        uiState = FilmUiState.Success(listOf(film)),
                        onRetry = {},
                        onFilmClick = { filmId ->
                            navController.navigate("film/$filmId")
                        },
                        onFavoritesClick = {}
                    )
                }

                composable("film/{filmId}") { backStackEntry ->
                    val filmId = backStackEntry.arguments?.getString("filmId")
                    Text("Detail ID: $filmId")
                }
            }
        }

        composeRule.onNodeWithText("My Neighbor Totoro")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithText("Detail ID: film-123")
            .assertIsDisplayed()
    }
}
