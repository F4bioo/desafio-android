package com.picpay.desafio.android.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.data.room.FavoriteDao
import com.picpay.desafio.android.data.room.PicPayDatabase
import com.picpay.desafio.android.utils.extensions.fromUserToFavoriteEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class GetFavoritesTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("provideTestPicPayDatabase")
    lateinit var db: PicPayDatabase

    @Inject
    @Named("provideTestUser")
    lateinit var user: User

    private lateinit var dao: FavoriteDao

    private lateinit var getFavorites: GetFavorites

    @Before
    fun setup() {
        hiltRule.inject()

        dao = db.favoriteDao()

        val repository = LocalRepository(dao)
        getFavorites = GetFavorites(repository)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun shouldPassWhenGettingFavorites(): Unit = runBlocking {
        dao.setFavorite(user.fromUserToFavoriteEntity())
        val dataState = getFavorites.invoke()
        val users = if (dataState is DataState.OnSuccess) dataState.data else null

        assertThat(users).contains(user)
    }
}
