package com.picpay.desafio.android.data.usecase


import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.utils.extensions.fromEntitiesToUsers
import javax.inject.Inject

class GetFavorites
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Empty<DataState<List<User>?>> {

    override suspend fun invoke(): DataState<List<User>?> {
        return try {
            DataState.OnSuccess(repository.getFavorites()?.fromEntitiesToUsers())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }
}
