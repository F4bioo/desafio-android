package com.picpay.desafio.android.data.usecase

import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.utils.extensions.fromUserToFavoriteEntity
import javax.inject.Inject

class SetFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<Boolean>, SetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Boolean> {
        return try {
            val favoriteEntity = params.user.fromUserToFavoriteEntity()

            val result = if (params.user.favorite) {
                repository.setFavorite(favoriteEntity) != -1L
            } else repository.delFavorite(favoriteEntity) != -1

            DataState.OnSuccess(result)
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val user: User
    )
}
