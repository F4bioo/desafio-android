package com.picpay.desafio.android.data.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.picpay.desafio.android.data.model.UserEntity
import com.picpay.desafio.android.data.room.PicPayDatabase
import com.picpay.desafio.android.ui.adapter.paging.UserRemoteMediator
import com.picpay.desafio.android.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class GetMediatorData
@Inject
constructor(
    private val getUsers: GetUsers,
    private val db: PicPayDatabase
) : BaseUseCase.Empty<Flow<PagingData<UserEntity>>> {

    override suspend fun invoke(): Flow<PagingData<UserEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = UserRemoteMediator(
                getUsers = getUsers,
                db = db
            ),
            pagingSourceFactory = { db.userDao().getAllUsers() }
        ).flow
    }
}
