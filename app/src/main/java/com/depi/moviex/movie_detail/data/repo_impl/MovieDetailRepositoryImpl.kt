package com.depi.moviex.movie_detail.data.repo_impl

import com.depi.moviex.common.MediaType
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie_detail.data.remote.api.MovieDetailApiService
import com.depi.moviex.movie_detail.data.remote.models.MovieDetailDto
import com.depi.moviex.movie_detail.data.remote.models.VideoDto
import com.depi.moviex.movie_detail.data.remote.models.toDomain
import com.depi.moviex.movie_detail.domain.models.CollectionMovie
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import com.depi.moviex.movie_detail.domain.repository.MovieDetailRepository
import com.depi.moviex.utils.K
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MovieDetailRepositoryImpl(
    private val movieDetailApiService: MovieDetailApiService,
    private val apiDetailMapper: ApiMapper<MovieDetail, MovieDetailDto>,
) : MovieDetailRepository {

    override fun fetchDetail(movieId: Int, mediaType: MediaType): Flow<Response<MovieDetail>> = flow {
        emit(Response.Loading())
        val language = K.getLanguageCode()
        val movieDetailDto = if (mediaType == MediaType.TV) {
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

    override fun fetchVideos(movieId: Int, mediaType: MediaType): Flow<Response<List<Video>>> = flow {
        emit(Response.Loading())
        val language = K.getLanguageCode()
        val movieDetailDto = if (mediaType == MediaType.TV) {
            movieDetailApiService.fetchTvDetail(movieId, language = language)
        } else {
            movieDetailApiService.fetchMovieDetail(movieId, language = language)
        }
        val videos = movieDetailDto.videos?.results
            ?.mapNotNull { (it as? VideoDto)?.toDomain() }
            ?: emptyList()
        emit(Response.Success(videos))
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e))
    }

    override fun getMovieCollection(collectionId: Int): Flow<Response<List<CollectionMovie>>> = flow {
        emit(Response.Loading<List<CollectionMovie>>())
        val language = K.getLanguageCode()
        val collectionDto = movieDetailApiService.fetchCollection(collectionId, language = language)
        val movies = collectionDto.parts?.map { it.toDomain() } ?: emptyList()
        emit(Response.Success(movies))
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e))
    }
}
