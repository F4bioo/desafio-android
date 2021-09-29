package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.model.UserDomain
import retrofit2.Response

interface Repository {

    interface RemoteData {
        suspend fun getUsers(): Response<List<UserDomain>?>?
    }
}
