package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.model.UserDomain
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository
@Inject
constructor(
    private val api: PicPayService
) : Repository.RemoteData {

    override suspend fun getUsers(): Response<List<UserDomain>?>? {
        return api.getUsers()
    }
}
