package com.depi.moviex.ui.theme.screens.moviedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.movie_detail.domain.models.Cast
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.ui.theme.BackgroundDark
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.utils.K

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCastClick: (Int, String) -> Unit = { _, _ -> },
    onCastMemberClick: (Int) -> Unit = {}
) {
    val state by movieDetailViewModel.movieDetailState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
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
                        color = Color.White
                    )
                }
            }
            state.movieDetail != null -> {
                MovieDetailContent(
                    movieDetail = state.movieDetail!!,
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
    onBackClick: () -> Unit,
    onCastClick: (Int, String) -> Unit,
    onCastMemberClick: (Int) -> Unit
) {
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
                                BackgroundDark.copy(alpha = 0.8f),
                                BackgroundDark
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
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
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
                    color = Color.White
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
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = movieDetail.releaseDate.take(4),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = movieDetail.runTime,
                        color = Color.White.copy(alpha = 0.7f)
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
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movieDetail.overview.ifEmpty { "No overview available." },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

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
                        color = Color.White
                    )
                    androidx.compose.material3.TextButton(
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

            if (movieDetail.reviews.isNotEmpty()) {
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
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
            color = Color.White,
            maxLines = 1
        )
        Text(
            text = cast.character,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.6f),
            maxLines = 1
        )
    }
}

@Composable
private fun ReviewItem(review: com.depi.moviex.movie_detail.domain.models.Review) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.05f),
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
                    tint = Color.Yellow,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = String.format("%.1f", review.rating),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.content,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 4
            )
        }
    }
}