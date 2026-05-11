package com.depi.moviex.movie.domain.repository

import androidx.paging.PagingData
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory
import com.depi.moviex.movie.domain.models.SearchResultItem
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun fetchMovies(category: MovieCategory): Flow<Response<List<Movie>>>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun searchAll(query: String): List<SearchResultItem>
    fun fetchTrendingMoviesPaged(category: String): Flow<PagingData<Movie>>
}
