package com.depi.moviex.ui.theme.screens.MovieDetails

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = viewModel()
) {

    LaunchedEffect(key1 = movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF12121A))) {
        when (val uiState = state) {
            is DetailsUiState.Success -> {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    MovieHeaderSection()

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray)
                            Text(text = " ${uiState.runtime}", color = Color.Gray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFAD31))
                            Text(text = " ${uiState.rating}", color = Color.Gray)
                        }

                        Row(modifier = Modifier.padding(vertical = 20.dp)) {
                            InfoColumn("Release date", uiState.releaseDate)
                            Spacer(modifier = Modifier.width(40.dp))
                            GenreColumn("Genre", uiState.genres)
                        }

                        Text(
                            text = "Synopsis",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            text = uiState.synopsis,
                            color = Color.LightGray,
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Members",
                            modifier = Modifier.padding(top = 20.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                    }
                }
            }
            is DetailsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is DetailsUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun MovieHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.DarkGray)
    ) {
        // هنا هتحط Coil Image لاحقاً
    }
}

@Composable
fun GenreColumn(label: String, genres: List<String>) {
    Column {
        Text(text = label, color = Color.White, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.padding(top = 4.dp)) {
            genres.forEach { genre ->
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color.White.copy(alpha = 0.1f), // خلفية شفافة للف futuristic look
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = genre,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String) {
    Column {
        Text(text = label, color = Color.White, fontWeight = FontWeight.Bold)
        Text(
            text = value,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}