package com.picpay.desafio.android.di

import android.content.Context
import android.content.SharedPreferences
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestPrefsModule {

    @Provides
    @Named("provideTestSharedPreferences")
    fun provideTestSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            Constants.PREFERENCES, Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named("provideTestPrefs")
    fun provideTestPrefs(
        @Named("provideTestSharedPreferences")
        prefs: SharedPreferences
    ): Prefs {
        return Prefs(prefs)
    }
}
