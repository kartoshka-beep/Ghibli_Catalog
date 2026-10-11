package com.example.ghiblicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ghiblicatalog.data.Film

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    film: Film?,
    isLoading: Boolean,
    error: String?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("О фильме") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Назад")
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            when {
                isLoading -> CircularProgressIndicator()

                error != null -> {
                    Text(error)
                    Button(onClick = onRetry) {
                        Text("Повторить")
                    }
                }

                film != null -> {
                    Text(
                        text = film.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text("Оригинальное описание:")
                    Text(film.description)
                    Text("Режиссёр: ${film.director}")
                    Text("Год выпуска: ${film.releaseDate}")

                    Button(onClick = onToggleFavorite) {
                        Text(
                            if (isFavorite) {
                                "Убрать из избранного"
                            } else {
                                "Добавить в избранное"
                            }
                        )
                    }
                }

                else -> Text("Информация о фильме отсутствует")
            }
        }
    }
}