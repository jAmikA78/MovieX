package com.depi.moviex.movie.data.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.depi.moviex.common.MediaType
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.paging.MoviePagingSource
import com.depi.moviex.movie.data.remote.api.MovieApiService
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.data.remote.models.MultiSearchDto
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory
import com.depi.moviex.movie.domain.models.SearchResultItem
import com.depi.moviex.movie.domain.repository.MovieRepository
import com.depi.moviex.utils.K
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
        val language = K.getLanguageCode()
        val page = when (category) {
            MovieCategory.TRENDING, MovieCategory.TOP_RATED, MovieCategory.UPCOMING -> 1
            MovieCategory.EGYPTIAN_MOVIES, MovieCategory.EGYPTIAN_TV, MovieCategory.KOREAN_TV -> (1..3).random()
            else -> (1..5).random()
        }
        val movieDto = when (category) {
            MovieCategory.TRENDING -> movieApiService.fetchTrendingMovies(page = page, language = language)
            MovieCategory.TOP_RATED -> movieApiService.fetchTopRatedMovies(page = page, language = language)
            MovieCategory.UPCOMING -> movieApiService.fetchUpcomingMovies(page = page, language = language)
            MovieCategory.TV_SHOWS -> movieApiService.fetchTvShows(page = page, language = language)
            MovieCategory.EGYPTIAN_TV -> movieApiService.fetchTvShows(
                originalLanguage = category.originalLanguage, originCountry = category.originCountry,
                page = page, language = language
            )
            MovieCategory.KOREAN_TV -> movieApiService.fetchTvShows(
                originalLanguage = category.originalLanguage,
                page = page, language = language
            )
            else -> movieApiService.fetchDiscoverMovies(
                genreId = category.genreId, originalLanguage = category.originalLanguage,
                originCountry = category.originCountry, page = page, language = language
            )
        }
        apiMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        val response = movieApiService.searchMovies(query = query, language = K.getLanguageCode())
        return apiMapper.mapToDomain(response)
    }

    override suspend fun searchAll(query: String): List<SearchResultItem> {
        val response = movieApiService.searchMulti(query = query, language = K.getLanguageCode())
        return response.results?.mapNotNull { result ->
            if (result == null || result.adult == true) return@mapNotNull null
            when (result.mediaType) {
                "movie" -> {
                    val movie = Movie(
                        backdropPath = result.backdropPath ?: "",
                        genreIds = result.genreIds?.map { MovieCategory.getGenreNameById(it ?: 0) } ?: emptyList(),
                        id = result.id ?: 0,
                        originalLanguage = result.originalLanguage ?: "",
                        originalTitle = result.originalTitle ?: result.title ?: "",
                        overview = result.overview ?: "",
                        popularity = result.popularity ?: 0.0,
                        posterPath = result.posterPath ?: "",
                        releaseDate = result.releaseDate ?: "",
                        title = result.title ?: result.name ?: "",
                        voteAverage = result.voteAverage ?: 0.0,
                        voteCount = result.voteCount ?: 0,
                        video = result.video ?: false,
                        mediaType = MediaType.MOVIE
                    )
                    SearchResultItem.MovieResult(movie)
                }
                "tv" -> {
                    val movie = Movie(
                        backdropPath = result.backdropPath ?: "",
                        genreIds = result.genreIds?.map { MovieCategory.getGenreNameById(it ?: 0) } ?: emptyList(),
                        id = result.id ?: 0,
                        originalLanguage = result.originalLanguage ?: "",
                        originalTitle = result.originalName ?: result.name ?: "",
                        overview = result.overview ?: "",
                        popularity = result.popularity ?: 0.0,
                        posterPath = result.posterPath ?: "",
                        releaseDate = result.firstAirDate ?: result.releaseDate ?: "",
                        title = result.name ?: result.title ?: result.originalName ?: "",
                        voteAverage = result.voteAverage ?: 0.0,
                        voteCount = result.voteCount ?: 0,
                        video = result.video ?: false,
                        mediaType = MediaType.TV
                    )
                    SearchResultItem.TvResult(movie)
                }
                "person" -> {
                    val knownFor = result.knownFor?.take(3)?.joinToString(", ") {
                        it.title ?: it.name ?: ""
                    } ?: ""
                    SearchResultItem.PersonResult(
                        id = result.id ?: 0,
                        name = result.name ?: result.title ?: "",
                        profilePath = result.profilePath,
                        knownFor = knownFor
                    )
                }
                else -> null
            }
        } ?: emptyList()
    }

    override fun fetchTrendingMoviesPaged(category: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                MoviePagingSource(movieApiService, apiMapper, category, K.getLanguageCode())
            }
        ).flow
    }
}
