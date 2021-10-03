package com.picpay.desafio.android.data.usecase

import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.utils.extensions.MapperExtension.fromUserToFavoriteEntity
import javax.inject.Inject

class SetFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<User?>, SetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<User?> {
        return try {
            val favoriteEntity = params.user.fromUserToFavoriteEntity()

            val result = if (params.user.favorite) {
                repository.setFavorite(favoriteEntity) > 0
            } else repository.delFavorite(favoriteEntity) > 0

            if (result) {
                DataState.OnSuccess(params.user)
            } else DataState.OnSuccess(null)

        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val user: User
    )
}
