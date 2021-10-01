package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.data.repository.RemoteRepository
import com.picpay.desafio.android.data.repository.Repository
import com.picpay.desafio.android.data.room.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRemoteRepository(
        api: PicPayService
    ): Repository.RemoteData {
        return RemoteRepository(api)
    }

    @Singleton
    @Provides
    fun provideLocalRepository(
        dao: FavoriteDao
    ): Repository.LocalData {
        return LocalRepository(dao)
    }
}
