package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.model.FavoriteEntity
import com.picpay.desafio.android.data.model.UserDomain
import retrofit2.Response

interface Repository {

    interface RemoteData {
        suspend fun getUsers(): Response<List<UserDomain>?>?
    }

    interface LocalData {
        suspend fun setFavorite(favorite: FavoriteEntity): Long

        suspend fun getFavorite(id: String): FavoriteEntity?

        suspend fun getFavorites(): List<FavoriteEntity>?

        suspend fun delFavorite(favorite: FavoriteEntity): Int
    }
}
