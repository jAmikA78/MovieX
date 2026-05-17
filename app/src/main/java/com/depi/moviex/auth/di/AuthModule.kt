package com.depi.moviex.auth.di

import android.content.Context
import com.depi.moviex.auth.data.remote.api.AuthApi
import com.depi.moviex.auth.data.repository.AuthRepositoryImpl
import com.depi.moviex.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        @ApplicationContext context: Context
    ): AuthRepository = AuthRepositoryImpl(authApi, context)
}
