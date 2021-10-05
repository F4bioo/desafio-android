package com.picpay.desafio.android.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.UserDomain
import com.picpay.desafio.android.data.repository.RemoteRepository
import dev.thiagosouto.butler.file.readFile
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

class GetUsersTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: RemoteRepository
    private lateinit var getUsers: GetUsers

    @Before
    fun setup() {
        repository = mock {
            getUsers = GetUsers(this.mock)
        }
    }

    private val success: Response<List<UserDomain>?>? = Response.success(arrayListOf())
    private val errorBody = readFile("json/error_body.json")
    private val error: Response<List<UserDomain>?>? = Response.error<List<UserDomain>?>(
        404, errorBody.toResponseBody("application/json".toMediaTypeOrNull())
    )

    @Test
    fun shouldPassWhenCallApiFromRepository(): Unit = runBlocking {
        getUsers.invoke()
        verify(repository, times(1)).getUsers()
    }

    @Test
    fun shouldReturnExceptionWhenGetNullResponse(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(null)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnException)
    }

    @Test
    fun shouldReturnErrorWhenGet404Response(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(error)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnError)
    }

    @Test
    fun shouldReturnSuccessWhenGetResponse(): Unit = runBlocking {
        whenever(repository.getUsers()).thenReturn(success)
        val dataState = getUsers.invoke()
        assertTrue(dataState is DataState.OnSuccess)
    }
}
