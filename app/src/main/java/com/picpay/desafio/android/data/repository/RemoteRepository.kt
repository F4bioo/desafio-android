package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.ApiService
import com.picpay.desafio.android.data.model.UserDomain
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository
@Inject
constructor(
    private val api: ApiService
) : Repository.RemoteData {
    override suspend fun getUsers(): Response<List<UserDomain>?>? {
        return api.getUsers()
    }
}
