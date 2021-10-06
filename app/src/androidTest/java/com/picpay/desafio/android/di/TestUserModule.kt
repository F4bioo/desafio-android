package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestUserModule {

    @Provides
    @Named("provideTestUser")
    fun provideTestUser(
    ): User {
        return User(
            "1",
            "Katheryn Winnick",
            "",
            "Lagertha"
        )
    }
}
