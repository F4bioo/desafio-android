package com.picpay.desafio.android.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.UserDomain
import com.picpay.desafio.android.data.repository.RemoteRepository
import dev.thiagosouto.butler.file.readFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetUsersTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: RemoteRepository = mock()
    private lateinit var getUsers: GetUsers

    private val success: Response<List<UserDomain>?>? = Response.success(arrayListOf())
    private val errorBody = readFile("json/error_body.json")
    private val error: Response<List<UserDomain>?>? = Response.error<List<UserDomain>?>(
        404, errorBody.toResponseBody("application/json".toMediaTypeOrNull())
    )

    @Test
    fun shouldPassWhenCallApiFromRepository(): Unit = runBlocking {
        getUsers = GetUsers(repository)
        getUsers.invoke()
        verify(repository, times(1)).getUsers()
    }

    @Test
    fun shouldReturnExceptionWhenGetNullResponse(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(null)
        getUsers = GetUsers(repository)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnException)
    }

    @Test
    fun shouldReturnErrorWhenGet404Response(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(error)
        getUsers = GetUsers(repository)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnError)
    }

    @Test
    fun shouldReturnSuccessWhenGetResponse(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(success)
        getUsers = GetUsers(repository)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnSuccess)
    }
}
