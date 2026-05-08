package com.depi.moviex.di

import com.depi.moviex.cast_member.data.mapper.CastMemberMapper
import com.depi.moviex.cast_member.data.remote.api.CastMemberApiService
import com.depi.moviex.cast_member.data.repo_impl.CastMemberRepositoryImpl
import com.depi.moviex.cast_member.domain.repository.CastMemberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CastMemberModule {

    @Provides
    @Singleton
    fun provideCastMemberRepository(
        apiService: CastMemberApiService,
        mapper: CastMemberMapper
    ): CastMemberRepository = CastMemberRepositoryImpl(
        apiService = apiService,
        mapper = mapper
    )

    @Provides
    @Singleton
    fun provideCastMemberMapper(): CastMemberMapper = CastMemberMapper()
}
