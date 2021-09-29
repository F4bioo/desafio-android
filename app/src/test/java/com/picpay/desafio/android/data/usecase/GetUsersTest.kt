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

    private val successMock: Response<List<UserDomain>?>? = Response.success(arrayListOf())
    private val errorBody = readFile("json/error_body.json")
    private val errorMock: Response<List<UserDomain>?>? = Response.error<List<UserDomain>?>(
        404, errorBody.toResponseBody("application/json".toMediaTypeOrNull())
    )

    @Test
    fun `should fetch data when call API from repository`(): Unit = runBlocking {
        getUsers = GetUsers(repository)
        getUsers.invoke()
        verify(repository, times(1)).getUsers()
    }

    @Test
    fun `should return exception when get a null response`(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(null)
        getUsers = GetUsers(repository)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnException)
    }

    @Test
    fun `should return error when get 404 response`(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(errorMock)
        getUsers = GetUsers(repository)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnError)
    }

    @Test
    fun `should return success when get response`(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(successMock)
        getUsers = GetUsers(repository)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnSuccess)
    }
}
