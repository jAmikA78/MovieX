package com.depi.moviex.ui.theme.screens.moviedetail

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie_detail.domain.models.Cast
import com.depi.moviex.movie_detail.domain.models.Crew
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.screens.moviedetail.YoutubePlayer
import com.depi.moviex.ui.theme.screens.watchlist.WatchlistViewModel
import com.depi.moviex.utils.K

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCastClick: (Int, String) -> Unit = { _, _ -> },
    onCastMemberClick: (Int) -> Unit = {}
) {
    val state by movieDetailViewModel.movieDetailState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            state.movieDetail != null -> {
                MovieDetailContent(
                    movieDetail = state.movieDetail!!,
                    videos = state.videos,
                    watchlistViewModel = watchlistViewModel,
                    onBackClick = onBackClick,
                    onCastClick = onCastClick,
                    onCastMemberClick = onCastMemberClick
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movieDetail: MovieDetail,
    videos: List<Video>,
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCastClick: (Int, String) -> Unit,
    onCastMemberClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val trailer = videos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
    val teaser = videos.firstOrNull { it.type == "Teaser" && it.site == "YouTube" }
    val videoToShow = trailer ?: teaser
    
    val isInWatchlist by watchlistViewModel.isInWatchlist(movieDetail.id).collectAsStateWithLifecycle(initialValue = false)
    val scope = rememberCoroutineScope()
    var isOverviewExpanded by remember { mutableStateOf(false) }
    
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

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 40.dp, start = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

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
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = PrimaryRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", movieDetail.voteAverage),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )
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
                scope.launch {
                    if (isInWatchlist) {
                        watchlistViewModel.removeFromWatchlist(movie)
                    } else {
                        watchlistViewModel.addToWatchlist(movie, "Detail")
                    }
                }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isInWatchlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle Watchlist",
                    tint = if (isInWatchlist) PrimaryRed else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (videoToShow != null) {
            val videoUrl = "https://www.youtube.com/watch?v=${videoToShow.key}"
            YoutubePlayer(videoUrl = videoUrl)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movieDetail.overview.ifEmpty { "No overview available." },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                maxLines = if (isOverviewExpanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis
            )

            if (movieDetail.overview.length > 150) {
                TextButton(
                    onClick = { isOverviewExpanded = !isOverviewExpanded },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (isOverviewExpanded) "Show Less" else "Show More",
                        color = PrimaryRed,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (movieDetail.cast.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cast",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(
                        onClick = { onCastClick(movieDetail.id, movieDetail.title) }
                    ) {
                        Text(
                            text = "See All",
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
                        CastItem(
                            cast = castMember,
                            onCastMemberClick = onCastMemberClick
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (movieDetail.crew.isNotEmpty()) {
                val keyJobs = listOf("Director", "Writer", "Producer", "Screenplay", "Original Music")
                val filteredCrew = movieDetail.crew.filter { it.job in keyJobs }.take(5)
                if (filteredCrew.isNotEmpty()) {
                    Text(
                        text = "Crew",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        filteredCrew.forEach { crewMember ->
                            CrewItem(crew = crewMember)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (movieDetail.reviews.isNotEmpty()) {
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                movieDetail.reviews.take(3).forEach { review ->
                    ReviewItem(review = review)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CastItem(
    cast: Cast,
    onCastMemberClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { onCastMemberClick(cast.id) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cast.profilePath?.let { "${K.BASE_IMAGE_URL}$it" })
                .crossfade(true)
                .build(),
            contentDescription = cast.name,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = cast.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
        Text(
            text = cast.character,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CrewItem(crew: Crew) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(crew.profilePath?.let { "${K.BASE_IMAGE_URL}$it" })
                .crossfade(true)
                .build(),
            contentDescription = crew.name,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = crew.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = crew.job,
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryRed,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
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
