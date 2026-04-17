package com.depi.moviex.di

import com.depi.moviex.cast_member.data.mapper.CastMemberMapper
import com.depi.moviex.cast_member.data.remote.api.CastMemberApiService
import com.depi.moviex.cast_member.data.repo_impl.CastMemberRepositoryImpl
import com.depi.moviex.cast_member.domain.repository.CastMemberRepository
import com.depi.moviex.utils.K
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CastMemberModule {

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

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

    @Provides
    @Singleton
    fun provideCastMemberApiService(): CastMemberApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CastMemberApiService::class.java)
    }
}