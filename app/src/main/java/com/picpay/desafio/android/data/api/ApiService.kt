package com.picpay.desafio.android.data.api

import com.picpay.desafio.android.data.model.UserDomain
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/users")
    suspend fun getUsers(
    ): Response<List<UserDomain>?>?
}
