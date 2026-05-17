package com.depi.moviex.di

import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie_detail.data.mapper_impl.MovieDetailMapperImpl
import com.depi.moviex.movie_detail.data.remote.api.MovieDetailApiService
import com.depi.moviex.movie_detail.data.remote.models.MovieDetailDto
import com.depi.moviex.movie_detail.data.repo_impl.MovieDetailRepositoryImpl
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.repository.MovieDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailModule {

    @Provides
    @Singleton
    fun provideMovieDetailRepository(
        movieDetailApiService: MovieDetailApiService,
        mapper: ApiMapper<MovieDetail, MovieDetailDto>,
    ): MovieDetailRepository = MovieDetailRepositoryImpl(
        movieDetailApiService = movieDetailApiService,
        apiDetailMapper = mapper,
    )

    @Provides
    @Singleton
    fun provideMovieDetailMapper(): ApiMapper<MovieDetail, MovieDetailDto> = MovieDetailMapperImpl()
}
