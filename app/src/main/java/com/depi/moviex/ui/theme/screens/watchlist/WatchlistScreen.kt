package com.depi.moviex.ui.theme.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.depi.moviex.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.components.ErrorText
import com.depi.moviex.ui.theme.components.LoadingIndicator
import com.depi.moviex.ui.theme.screens.home.components.MovieCoverImage
import kotlinx.coroutines.launch

@Composable
fun WatchlistScreen(
    modifier: Modifier = Modifier,
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    onMovieClick: (id: Int, mediaType: String) -> Unit = { _, _ -> }
) {
    val state by watchlistViewModel.watchlistState.collectAsStateWithLifecycle()
    val filterAll = stringResource(R.string.filter_all)
    var selectedCategory by remember { mutableStateOf(filterAll) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.watchlist_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 50.dp, bottom = 16.dp)
        )

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
                            if (category == filterAll) {
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
                LoadingIndicator()
            }
            state.error != null -> {
                ErrorText(message = state.error)
            }
            state.watchlistMovies.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.watchlist_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = state.watchlistMovies.chunked(2),
                        key = { index, _ -> index.toString() }
                    ) { _, rowMovies ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowMovies.forEach { movie ->
                                MovieCoverImage(
                                    movie = movie,
                                    onMovieClick = onMovieClick,
                                    isInWatchlist = true,
                                    onHeartClick = {},
                                    onRemoveFromWatchlist = { movieToRemove ->
                                        scope.launch {
                                            watchlistViewModel.removeFromWatchlist(movieToRemove)
                                        }
                                    }
                                )
                            }
                        }
                        }
                    }
                }
            }
        }
    }
}
