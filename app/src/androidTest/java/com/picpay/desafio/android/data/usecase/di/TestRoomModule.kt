package com.picpay.desafio.android.data.usecase.di

import android.content.Context
import androidx.room.Room
import com.picpay.desafio.android.data.room.PicPayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestRoomModule {

    @Provides
    @Named("provideTestPicPayDatabase")
    fun provideTestPicPayDatabase(
        @ApplicationContext context: Context
    ): PicPayDatabase {
        return Room.inMemoryDatabaseBuilder(
            context, PicPayDatabase::class.java
        ).allowMainThreadQueries().build()
    }
}
