package com.depi.moviex.di

import com.depi.moviex.auth.data.remote.api.AuthApi
import com.depi.moviex.cast_member.data.remote.api.CastMemberApiService
import com.depi.moviex.movie.data.remote.api.MovieApiService
import com.depi.moviex.movie_detail.data.remote.api.MovieDetailApiService
import com.depi.moviex.utils.K
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("kotlinSerialization")
    fun provideKotlinSerializationRetrofit(client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    @Named("gson")
    fun provideGsonRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(@Named("kotlinSerialization") retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDetailApiService(@Named("kotlinSerialization") retrofit: Retrofit): MovieDetailApiService {
        return retrofit.create(MovieDetailApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCastMemberApiService(@Named("kotlinSerialization") retrofit: Retrofit): CastMemberApiService {
        return retrofit.create(CastMemberApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("gson") gsonRetrofit: Retrofit): AuthApi {
        return gsonRetrofit.create(AuthApi::class.java)
    }
}
