package com.depi.moviex.movie.data.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.paging.MoviePagingSource
import com.depi.moviex.movie.data.remote.api.MovieApiService
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory
import com.depi.moviex.movie.domain.repository.MovieRepository
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val movieApiService: MovieApiService,
    private val apiMapper: ApiMapper<List<Movie>, MovieDto>
) : MovieRepository {

    override fun fetchMovies(category: MovieCategory): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movieDto = when (category) {
            MovieCategory.TRENDING -> movieApiService.fetchTrendingMovies()
            MovieCategory.TV_SHOWS -> movieApiService.fetchTvShows()
            else -> movieApiService.fetchDiscoverMovies(genreId = category.genreId)
        }
        apiMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        val response = movieApiService.searchMovies(query = query)
        return apiMapper.mapToDomain(response)
    }

    override fun fetchTrendingMoviesPaged(category: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                MoviePagingSource(movieApiService, apiMapper, category)
            }
        ).flow
    }
}
