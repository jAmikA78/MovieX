package com.depi.moviex.ui.theme.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.depi.moviex.movie.data.remote.models.MovieDto

private val MovieDto.id: Int
    get() {
        TODO()
    }

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val movies by viewModel.movies.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for movies...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (movies.isEmpty() && searchQuery.length > 2) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No movies found")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies) { movie ->

                    MovieItem(movie = movie, onClick = { onMovieClick(movie.id) })
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieDto, onClick: () -> Unit) {
    TODO("Not yet implemented")
}

