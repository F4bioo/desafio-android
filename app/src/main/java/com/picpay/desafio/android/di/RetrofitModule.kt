package com.picpay.desafio.android.di

import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideLevel(
    ): HttpLoggingInterceptor.Level {
        return HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpLogging(
        level: HttpLoggingInterceptor.Level
    ): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = level
        return logging
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addInterceptor(logging)
        }
        return client.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    @Singleton
    @Provides
    fun providePicPayService(
        retrofit: Retrofit.Builder
    ): PicPayService {
        return retrofit
            .build()
            .create(PicPayService::class.java)
    }
}
