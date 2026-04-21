package com.depi.moviex.movie.data.repository

import androidx.paging.PagingData
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.repository.MovieRepository
import com.depi.moviex.movie_detail.data.remote.api.MovieDetailApiService
import com.depi.moviex.movie_detail.data.remote.models.MovieResponse
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieDetailApiService
) : MovieRepository {
    override fun fetchDiscoverMovies(): Flow<Response<List<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun fetchTrendingMovies(): Flow<Response<List<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun fetchTvShows(): Flow<Response<List<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun fetchActionMovies(): Flow<Response<List<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun fetchDramaMovies(): Flow<Response<List<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun fetchComedyMovies(): Flow<Response<List<Movie>>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovies(query: String): Result<MovieResponse> {
        return try {
            val response = apiService.searchMovies(query = query)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun fetchTrendingMoviesPaged(category: String): Flow<PagingData<Movie>> {
        TODO("Not yet implemented")
    }
}