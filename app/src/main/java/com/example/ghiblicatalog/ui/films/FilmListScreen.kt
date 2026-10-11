package com.example.ghiblicatalog.ui.films

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ghiblicatalog.data.Film

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    uiState: FilmUiState,
    onRetry: () -> Unit,
    onFilmClick: (String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Studio Ghibli") },
                actions = {
                    Button(onClick = onFavoritesClick) {
                        Text("Избранное")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск фильмов") },
                placeholder = { Text("Введите название") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            when (uiState) {
                FilmUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                FilmUiState.Empty -> {
                    Message(
                        text = "Фильмы не найдены",
                        buttonText = null,
                        onClick = {}
                    )
                }

                is FilmUiState.Error -> {
                    Message(
                        text = "Не удалось загрузить фильмы.\n${uiState.message}",
                        buttonText = "Повторить",
                        onClick = onRetry
                    )
                }

                is FilmUiState.Success -> {
                    val filteredFilms = uiState.films.filter {
                        it.title.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredFilms.isEmpty()) {
                        Text(
                            text = "Ничего не найдено",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    } else {
                        FilmList(
                            films = filteredFilms,
                            onFilmClick = onFilmClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilmList(
    films: List<Film>,
    onFilmClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = films,
            key = { it.id }
        ) { film ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFilmClick(film.id) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = film.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Режиссёр: ${film.director}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Год выпуска: ${film.releaseDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Message(
    text: String,
    buttonText: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = text)

        if (buttonText != null) {
            Button(onClick = onClick) {
                Text(buttonText)
            }
        }
    }
}