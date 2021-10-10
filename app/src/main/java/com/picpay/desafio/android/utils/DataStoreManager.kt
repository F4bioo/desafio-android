package com.picpay.desafio.android.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore
        by preferencesDataStore(name = Constants.DATA_STORE)

class DataStoreManager
@Inject
constructor(
    @ApplicationContext private val context: Context
) {

    private suspend fun <T> DataStore<Preferences>.getFromLocalStorage(
        prefsKey: Preferences.Key<T>,
        responseFunc: T.() -> Unit,
    ) {
        data.catch { e ->
            when (e) {
                is IOException -> emit(emptyPreferences())
                else -> throw e
            }
        }.map { prefs ->
            prefs[prefsKey]
        }.collect {
            it?.let { responseFunc.invoke(it as T) }
        }
    }

    suspend fun <T> storeValue(
        prefsKey: Preferences.Key<T>, value: T
    ) {
        context.dataStore.edit { prefs ->
            prefs[prefsKey] = value
        }
    }

    suspend fun <T> readValue(
        prefsKey: Preferences.Key<T>,
        defValue: T,
        responseFunc: T.() -> Unit,
    ) {
        // For each stored value the read value will be notified
        context.dataStore.getFromLocalStorage(prefsKey) {
            responseFunc.invoke(this ?: defValue)
        }
    }
}
