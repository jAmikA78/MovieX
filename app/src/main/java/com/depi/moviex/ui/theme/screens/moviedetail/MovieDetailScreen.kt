package com.depi.moviex.ui.theme.screens.moviedetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.common.MediaType
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie_detail.domain.models.CollectionMovie
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.components.ActorCard
import com.depi.moviex.ui.theme.components.BackButton
import com.depi.moviex.ui.theme.components.ErrorText
import com.depi.moviex.ui.theme.components.ExpandableText
import com.depi.moviex.ui.theme.components.LoadingIndicator
import com.depi.moviex.ui.theme.components.RemoveFromFavoritesDialog
import com.depi.moviex.ui.theme.components.SectionTitle
import com.depi.moviex.ui.theme.components.StarRating
import com.depi.moviex.ui.theme.screens.favorites.FavoriteViewModel
import com.depi.moviex.utils.K
import com.depi.moviex.LocalIsGuest
import com.depi.moviex.LocalOnLoginClick
import com.depi.moviex.ui.theme.components.LoginRequiredDialog
import com.depi.moviex.utils.AdManager
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCastClick: (Int, String, MediaType) -> Unit = { _, _, _ -> },
    onCastMemberClick: (Int) -> Unit = {},
    onMovieClick: (Int, String) -> Unit = { _, _ -> }
) {
    val state by movieDetailViewModel.movieDetailState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading -> {
                LoadingIndicator()
            }
            state.error != null -> {
                ErrorText(message = state.error)
            }
            state.movieDetail != null -> {
                MovieDetailContent(
                    movieDetail = state.movieDetail!!,
                    videos = state.videos,
                    favoriteViewModel = favoriteViewModel,
                    onBackClick = onBackClick,
                    onCastClick = onCastClick,
                    onCastMemberClick = onCastMemberClick,
                    mediaType = state.mediaType,
                    isReminderSet = state.isReminderSet,
                    onToggleReminder = { movieDetailViewModel.toggleReminder(it, state.movieDetail!!.posterPath, state.movieDetail!!.releaseDate) },
                    onMovieClick = onMovieClick,
                    collectionMovies = state.collectionMovies,
                    collectionName = state.collectionName,
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movieDetail: MovieDetail,
    videos: List<Video>,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCastClick: (Int, String, MediaType) -> Unit,
    onCastMemberClick: (Int) -> Unit,
    mediaType: MediaType = MediaType.MOVIE,
    isReminderSet: Boolean = false,
    onToggleReminder: (String) -> Unit = {},
    onMovieClick: (Int, String) -> Unit = { _, _ -> },
    collectionMovies: List<CollectionMovie> = emptyList(),
    collectionName: String? = null,
) {
    val context = LocalContext.current
    val trailer = videos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
    val teaser = videos.firstOrNull { it.type == "Teaser" && it.site == "YouTube" }
    val videoToShow = trailer ?: teaser
    
    val isInFavorite by favoriteViewModel.isInFavorite(movieDetail.id).collectAsStateWithLifecycle(initialValue = false)
    val scope = rememberCoroutineScope()
    var isOverviewExpanded by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    val isGuest = LocalIsGuest.current
    val onLoginClick = LocalOnLoginClick.current
    
    val movie = Movie(
        id = movieDetail.id,
        title = movieDetail.title,
        posterPath = movieDetail.posterPath ?: "",
        backdropPath = movieDetail.backdropPath ?: "",
        releaseDate = movieDetail.releaseDate,
        voteAverage = movieDetail.voteAverage,
        voteCount = movieDetail.voteCount,
        genreIds = emptyList(),
        originalLanguage = "",
        originalTitle = movieDetail.title,
        overview = movieDetail.overview,
        popularity = 0.0,
        video = false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${K.BASE_IMAGE_URL}${movieDetail.backdropPath}")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            BackButton(onClick = onBackClick)

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = movieDetail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarRating(rating = movieDetail.voteAverage)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = movieDetail.releaseDate.take(4),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = movieDetail.runTime,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onToggleReminder(movieDetail.title) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isReminderSet) Icons.Filled.NotificationsActive else Icons.Filled.NotificationsNone,
                            contentDescription = if (isReminderSet) "Reminder set" else "Set reminder",
                            tint = if (isReminderSet) PrimaryRed else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movieDetail.genreIds.take(3).forEach { genre ->
                        Surface(
                            color = PrimaryRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = genre,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = PrimaryRed,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
            
            // Heart icon for watchlist
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .clickable {
                if (isInFavorite) {
                    showRemoveDialog = true
                } else if (isGuest) {
                    showLoginDialog = true
                } else {
                    scope.launch {
                        favoriteViewModel.addToFavorite(movie, FavoriteViewModel.CATEGORY_DETAIL)
                    }
                }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isInFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.toggle_favorite),
                    tint = if (isInFavorite) PrimaryRed else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SectionTitle(text = stringResource(R.string.overview), bottomSpacer = false)
            Spacer(modifier = Modifier.height(8.dp))
            ExpandableText(
                text = movieDetail.overview,
                emptyMessage = stringResource(R.string.no_overview_available)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (videoToShow != null) {
                val videoUrl = "https://www.youtube.com/watch?v=${videoToShow.key}"
                YoutubePlayer(videoUrl = videoUrl)
                Spacer(modifier = Modifier.height(16.dp))
            }

            WatchButton(imdbId = movieDetail.imdbId)
            Spacer(modifier = Modifier.height(24.dp))

            if (movieDetail.cast.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.cast_label),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(
                        onClick = { onCastClick(movieDetail.id, movieDetail.title, mediaType) }
                    ) {
                        Text(
                            text = stringResource(R.string.see_all),
                            color = PrimaryRed,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PrimaryRed,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    movieDetail.cast.take(10).forEach { castMember ->
                        ActorCard(
                            name = castMember.name,
                            profilePath = castMember.profilePath,
                            role = castMember.character,
                            onClick = { onCastMemberClick(castMember.id) },
                            compact = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (movieDetail.crew.isNotEmpty()) {
                val keyJobs = listOf("Director", "Writer", "Producer", "Screenplay", "Original Music")
                val filteredCrew = movieDetail.crew.filter { it.job in keyJobs }.take(5)
                if (filteredCrew.isNotEmpty()) {
                    SectionTitle(text = stringResource(R.string.crew))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        filteredCrew.forEach { crewMember ->
                            ActorCard(
                                name = crewMember.name,
                                profilePath = crewMember.profilePath,
                                role = crewMember.job,
                                compact = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (movieDetail.reviews.isNotEmpty()) {
                SectionTitle(text = stringResource(R.string.reviews))
                movieDetail.reviews.take(3).forEach { review ->
                    ReviewItem(review = review)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (collectionMovies.isNotEmpty()) {
                collectionName?.let { name ->
                    SectionTitle(text = name)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(collectionMovies.size) { index ->
                        val movie = collectionMovies[index]
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .clickable { onMovieClick(movie.id, MediaType.MOVIE.value) },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 120.dp, height = 180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("${K.BASE_IMAGE_URL}${movie.posterPath}")
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = movie.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%.1f", movie.voteAverage),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

    if (showRemoveDialog) {
        RemoveFromFavoritesDialog(
            title = movieDetail.title,
            onDismiss = { showRemoveDialog = false },
            onConfirm = {
                scope.launch {
                    favoriteViewModel.removeFromFavorite(movie)
                }
                showRemoveDialog = false
            }
        )
    }

    if (showLoginDialog) {
        LoginRequiredDialog(
            onDismiss = { showLoginDialog = false },
            onLoginClick = onLoginClick
        )
    }

    }
}
}



@Composable
private fun ReviewItem(review: com.depi.moviex.movie_detail.domain.models.Review) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = String.format("%.1f", review.rating),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 4
            )
        }
    }
}

@Composable
private fun WatchButton(imdbId: String) {
    if (imdbId.isBlank()) return

    val context = LocalContext.current
    val activity = remember { context as? Activity }
    val playUrl = "https://www.playimdb.com/title/$imdbId/"

    LaunchedEffect(Unit) {
        AdManager.loadInterstitial(context)
    }

    Button(
        onClick = {
            activity?.let {
                AdManager.showInterstitial(it) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playUrl))
                    context.startActivity(intent)
                }
            } ?: run {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playUrl))
                context.startActivity(intent)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE54E3C))
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.watch),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
