package com.picpay.desafio.android.data.usecase

import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.api.parseResponse
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.RemoteRepository
import com.picpay.desafio.android.utils.extensions.fromDomainsToUsers
import javax.inject.Inject

class GetUsers
@Inject
constructor(
    private val repository: RemoteRepository
) : BaseUseCase.Empty<DataState<List<User>>> {

    override suspend fun invoke(): DataState<List<User>> {
        return when (val response = repository.getUsers().parseResponse()) {
            is DataState.OnSuccess -> response.data?.let {
                DataState.OnSuccess(it.fromDomainsToUsers())
            } ?: DataState.OnException(Exception("User list is empty!"))

            is DataState.OnError -> DataState.OnError(response.errorBody, response.code)

            is DataState.OnException -> DataState.OnException(response.e)
        }
    }
}
