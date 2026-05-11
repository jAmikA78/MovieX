package com.depi.moviex.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.remote.api.MovieApiService
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory
import com.depi.moviex.utils.K

class MoviePagingSource(
    private val movieApiService: MovieApiService,
    private val apiMapper: ApiMapper<List<Movie>, MovieDto>,
    private val category: String,
    private val languageCode: String = K.getLanguageCode()
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
                MovieCategory.TRENDING -> movieApiService.fetchTrendingMovies(page = page, language = languageCode)
                MovieCategory.TOP_RATED -> movieApiService.fetchTopRatedMovies(page = page, language = languageCode)
                MovieCategory.UPCOMING -> movieApiService.fetchUpcomingMovies(page = page, language = languageCode)
                MovieCategory.TV_SHOWS -> movieApiService.fetchTvShows(page = page, language = languageCode)
                MovieCategory.EGYPTIAN_TV -> movieApiService.fetchTvShows(
                    originalLanguage = movieCategory.originalLanguage, originCountry = movieCategory.originCountry,
                    page = page, language = languageCode
                )
                MovieCategory.KOREAN_TV -> movieApiService.fetchTvShows(
                    originalLanguage = movieCategory.originalLanguage,
                    page = page, language = languageCode
                )
                else -> movieApiService.fetchDiscoverMovies(
                    genreId = movieCategory?.genreId, originalLanguage = movieCategory?.originalLanguage,
                    originCountry = movieCategory?.originCountry, page = page, language = languageCode
                )
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
