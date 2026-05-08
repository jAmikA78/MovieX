package com.depi.moviex.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.remote.api.MovieApiService
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory

class MoviePagingSource(
    private val movieApiService: MovieApiService,
    private val apiMapper: ApiMapper<List<Movie>, MovieDto>,
    private val category: String
) : PagingSource<Int, Movie>() {

    private val categoryMap = MovieCategory.entries.associateBy { it.displayName }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val movieCategory = categoryMap[category]
            val response = when (movieCategory) {
                MovieCategory.TRENDING -> movieApiService.fetchTrendingMovies(page = page)
                MovieCategory.TV_SHOWS -> movieApiService.fetchTvShows(page = page)
                else -> movieApiService.fetchDiscoverMovies(genreId = movieCategory?.genreId, page = page)
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
