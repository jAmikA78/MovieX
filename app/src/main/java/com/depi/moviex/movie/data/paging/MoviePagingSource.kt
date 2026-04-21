package com.depi.moviex.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.remote.api.MovieApiService
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.domain.models.Movie

class MoviePagingSource(
    private val movieApiService: MovieApiService,
    private val apiMapper: ApiMapper<List<Movie>, MovieDto>,
    private val category: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = when (category) {
                "Trending now" -> movieApiService.fetchTrendingMovies(page = page)
                "Most Watched" -> movieApiService.fetchTrendingMovies(page = page)
                "TV Shows" -> movieApiService.fetchTvShows(page = page)
                "Action" -> movieApiService.fetchActionMovies(page = page)
                "Drama" -> movieApiService.fetchDramaMovies(page = page)
                "Comedy" -> movieApiService.fetchComedyMovies(page = page)
                else -> movieApiService.fetchDiscoverMovies(page = page)
            }
            
            val movies = apiMapper.mapToDomain(response)
            
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
