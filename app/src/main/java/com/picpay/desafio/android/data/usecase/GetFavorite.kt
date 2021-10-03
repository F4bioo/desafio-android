package com.picpay.desafio.android.data.usecase

import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.utils.extensions.MapperExtension.fromUserEntityToUser
import javax.inject.Inject

class GetFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<User?>, GetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<User?> {
        return try {
            DataState.OnSuccess(repository.getFavorite(params.id)?.fromUserEntityToUser())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val id: String
    )
}
