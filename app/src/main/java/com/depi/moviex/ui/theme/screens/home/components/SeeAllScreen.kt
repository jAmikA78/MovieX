package com.depi.moviex.ui.theme.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.screens.favorites.FavoriteViewModel
import com.depi.moviex.utils.K
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R
import com.depi.moviex.LocalIsGuest
import com.depi.moviex.LocalOnLoginClick
import com.depi.moviex.ui.theme.components.LoginRequiredDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllScreen(
    categoryTitle: String,
    onMovieClick: (Int, String) -> Unit,
    onBackClick: () -> Unit,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    viewModel: SeeAllViewModel = hiltViewModel()
) {
    val movies = viewModel.getPagedMovies(categoryTitle).collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movies.itemCount) { index ->
                    val movie = movies[index]
                    if (movie != null) {
                        MovieGridItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id, movie.mediaType.value) },
                            favoriteViewModel = favoriteViewModel,
                            categoryTitle = categoryTitle
                        )
                    }
                }

                if (movies.loadState.append is LoadState.Loading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            if (movies.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun MovieGridItem(
    movie: Movie,
    onClick: () -> Unit,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    categoryTitle: String = ""
) {
    val isInFavorite by favoriteViewModel.isInFavorite(movie.id).collectAsStateWithLifecycle(initialValue = false)
    val scope = rememberCoroutineScope()
    var showLoginDialog by remember { mutableStateOf(false) }
    val isGuest = LocalIsGuest.current
    val onLoginClick = LocalOnLoginClick.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val isInFavorite by favoriteViewModel.isInFavorite(movie.id).collectAsStateWithLifecycle(initialValue = false)

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${K.BASE_IMAGE_URL}${movie.posterPath}")
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )

            Text(
                text = movie.title,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Heart icon for watchlist
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable {
                if (isInFavorite) {
                    scope.launch {
                        favoriteViewModel.removeFromFavorite(movie)
                    }
                } else if (isGuest) {
                    showLoginDialog = true
                } else {
                    scope.launch {
                        favoriteViewModel.addToFavorite(movie, categoryTitle)
                    }
                }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isInFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.toggle_favorite),
                    tint = if (isInFavorite) PrimaryRed else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (showLoginDialog) {
            LoginRequiredDialog(
                onDismiss = { showLoginDialog = false },
                onLoginClick = onLoginClick
            )
        }
    }
}
