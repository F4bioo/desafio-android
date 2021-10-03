package com.picpay.desafio.android.ui.adapter.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.RemoteKeyEntity
import com.picpay.desafio.android.data.model.UserEntity
import com.picpay.desafio.android.data.room.PicPayDatabase
import com.picpay.desafio.android.data.usecase.GetUsers
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.extensions.fromDomainsToEntities
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class UserRemoteMediator(
    private val getUsers: GetUsers,
    private val db: PicPayDatabase
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }

        try {
            val response = getUsers.invoke()
            val users = if (response is DataState.OnSuccess) response.data else arrayListOf()
            val isEndOfList = users.isEmpty()

            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.userDao().delAllUsers()
                    db.keyDao().delAllRemoteKeys()
                }

                val prevKey = if (page == Constants.PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = users.map {
                    RemoteKeyEntity(it.id, prevKey = prevKey, nextKey = nextKey)
                }

                db.userDao().setAllUsers(users.fromDomainsToEntities())
                db.keyDao().setAllRemoteKeys(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                return remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    // get the closest remote key inserted which had the data
    private suspend fun getClosestRemoteKey(state: PagingState<Int, UserEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.keyDao().getRemoteKey(repoId)
            }
        }
    }

    // get the last remote key inserted which had the data
    private suspend fun getLastRemoteKey(state: PagingState<Int, UserEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { user -> db.keyDao().getRemoteKey(user.id) }
    }

    // get the first remote key inserted which had the data
    private suspend fun getFirstRemoteKey(state: PagingState<Int, UserEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { user -> db.keyDao().getRemoteKey(user.id) }
    }
}
