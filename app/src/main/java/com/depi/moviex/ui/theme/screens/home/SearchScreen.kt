package com.depi.moviex.ui.theme.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.depi.moviex.movie.data.remote.models.Result
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.utils.K

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val movies by viewModel.movies.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            placeholder = { 
                Text(
                    text = "Search for movies...", 
                    color = MaterialTheme.colorScheme.onSurfaceVariant 
                ) 
            },
            leadingIcon = { 
                Icon(
                    imageVector = Icons.Default.Search, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                ) 
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = PrimaryRed,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = PrimaryRed,
                focusedLabelColor = PrimaryRed,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (movies.isEmpty() && searchQuery.length > 2) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No movies found", 
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onClick = { movie.id?.let { onMovieClick(it) } })
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Result, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = K.BASE_IMAGE_URL + movie.posterPath,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = movie.title ?: "Unknown",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
