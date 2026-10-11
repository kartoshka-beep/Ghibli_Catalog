package com.example.ghiblicatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ghiblicatalog.ui.detail.FilmDetailScreen
import com.example.ghiblicatalog.ui.detail.FilmDetailViewModel
import com.example.ghiblicatalog.ui.favorites.FavoritesScreen
import com.example.ghiblicatalog.ui.favorites.FavoritesViewModel
import com.example.ghiblicatalog.ui.films.FilmListScreen
import com.example.ghiblicatalog.ui.films.FilmViewModel
import com.example.ghiblicatalog.ui.theme.GhibliCatalogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GhibliCatalogTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "films"
                    ) {
                        composable("films") {
                            val filmViewModel: FilmViewModel = hiltViewModel()
                            val uiState by filmViewModel.uiState.collectAsState()

                            FilmListScreen(
                                uiState = uiState,
                                onRetry = {
                                    filmViewModel.loadFilms()
                                },
                                onFilmClick = { filmId ->
                                    navController.navigate("film/$filmId")
                                },
                                onFavoritesClick = {
                                    navController.navigate("favorites")
                                }
                            )
                        }

                        composable(
                            route = "film/{filmId}",
                            arguments = listOf(
                                navArgument("filmId") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val detailViewModel: FilmDetailViewModel =
                                hiltViewModel()

                            val film by detailViewModel.film.collectAsState()
                            val isLoading by detailViewModel.isLoading.collectAsState()
                            val error by detailViewModel.error.collectAsState()
                            val isFavorite by detailViewModel.isFavorite.collectAsState()

                            FilmDetailScreen(
                                film = film,
                                isLoading = isLoading,
                                error = error,
                                isFavorite = isFavorite,
                                onToggleFavorite = {
                                    detailViewModel.toggleFavorite()
                                },
                                onRetry = {
                                    detailViewModel.loadFilm()
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("favorites") {
                            val favoritesViewModel: FavoritesViewModel =
                                hiltViewModel()

                            val favorites by favoritesViewModel.favorites
                                .collectAsState()

                            FavoritesScreen(
                                favorites = favorites,
                                onFilmClick = { filmId ->
                                    navController.navigate("film/$filmId")
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}