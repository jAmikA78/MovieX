package com.depi.moviex.ui.theme.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.screens.home.components.MovieCoverImage

@Composable
fun WatchlistScreen(
    modifier: Modifier = Modifier,
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    onMovieClick: (id: Int) -> Unit = {}
) {
    val state by watchlistViewModel.watchlistState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Watchlist",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 50.dp, bottom = 16.dp)
        )

        // Category filter chips
        if (state.availableCategories.size > 1) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(state.availableCategories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            if (category == "All") {
                                watchlistViewModel.loadWatchlistMovies()
                            } else {
                                watchlistViewModel.loadMoviesByCategory(category)
                            }
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryRed.copy(alpha = 0.2f),
                            selectedLabelColor = PrimaryRed,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryRed)
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "An error occurred",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            state.watchlistMovies.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your watchlist is empty",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.watchlistMovies) { movie ->
                        WatchlistMovieItem(
                            movie = movie,
                            onMovieClick = { onMovieClick(movie.id) },
                            watchlistViewModel = watchlistViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistMovieItem(
    movie: Movie,
    onMovieClick: () -> Unit,
    watchlistViewModel: WatchlistViewModel = hiltViewModel()
) {
    val isInWatchlist by watchlistViewModel.isInWatchlist(movie.id).collectAsStateWithLifecycle(initialValue = false)
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onMovieClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w200${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .size(width = 60.dp, height = 90.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = movie.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            Text(
                text = movie.releaseDate.take(4),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

            // Heart icon to remove from watchlist
            IconButton(
                onClick = {
                    scope.launch {
                        if (isInWatchlist) {
                            watchlistViewModel.removeFromWatchlist(movie)
                        }
                    }
                },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (isInWatchlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Remove from Watchlist",
                tint = if (isInWatchlist) PrimaryRed else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
