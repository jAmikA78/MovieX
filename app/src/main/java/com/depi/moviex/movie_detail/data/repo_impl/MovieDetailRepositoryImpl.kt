package com.depi.moviex.movie_detail.data.repo_impl


import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie_detail.data.remote.api.MovieDetailApiService
import com.depi.moviex.movie_detail.data.remote.models.MovieDetailDto
import com.depi.moviex.movie_detail.data.remote.models.toDomain
import com.depi.moviex.utils.Response
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import com.depi.moviex.movie_detail.domain.repository.MovieDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.Locale

class MovieDetailRepositoryImpl(
    private val movieDetailApiService: MovieDetailApiService,
    private val apiDetailMapper: ApiMapper<MovieDetail, MovieDetailDto>,
    private val apiMovieMapper: ApiMapper<List<Movie>, MovieDto>,
) : MovieDetailRepository {

    private fun getLanguageCode(): String {
        val lang = Locale.getDefault().language
        return if (lang == "ar") "ar-SA" else "en-US"
    }

    override fun fetchDetail(movieId: Int, mediaType: String): Flow<Response<MovieDetail>> = flow {
        emit(Response.Loading())
        val language = getLanguageCode()
        val movieDetailDto = if (mediaType == "tv") {
            movieDetailApiService.fetchTvDetail(movieId, language = language)
        } else {
            movieDetailApiService.fetchMovieDetail(movieId, language = language)
        }
        apiDetailMapper.mapToDomain(movieDetailDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e))
    }

    override fun fetchVideos(movieId: Int, mediaType: String): Flow<Response<List<Video>>> = flow {
        emit(Response.Loading())
        val language = getLanguageCode()
        val movieDetailDto = if (mediaType == "tv") {
            movieDetailApiService.fetchTvDetail(movieId, language = language)
        } else {
            movieDetailApiService.fetchMovieDetail(movieId, language = language)
        }
        val videos = movieDetailDto.videos?.results?.mapNotNull { (it as? com.depi.moviex.movie_detail.data.remote.models.VideoDto)?.toDomain() } ?: emptyList<Video>()
        emit(Response.Success(videos))
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e))
    }

    override fun fetchMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val language = getLanguageCode()
        val movieDto = movieDetailApiService.fetchMovie(language = language)
        apiMovieMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e))
    }

}