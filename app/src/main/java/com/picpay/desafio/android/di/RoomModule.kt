package com.picpay.desafio.android.di

import android.content.Context
import androidx.room.Room
import com.picpay.desafio.android.data.room.PicPayDatabase
import com.picpay.desafio.android.data.room.RemoteKeyDao
import com.picpay.desafio.android.data.room.UserDao
import com.picpay.desafio.android.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providePicPayDatabase(
        @ApplicationContext context: Context
    ): PicPayDatabase {
        return Room.databaseBuilder(
            context, PicPayDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDao(
        db: PicPayDatabase
    ): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(
        db: PicPayDatabase
    ): RemoteKeyDao {
        return db.keyDao()
    }
}
