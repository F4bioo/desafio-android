package com.picpay.desafio.android.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.data.room.FavoriteDao
import com.picpay.desafio.android.data.room.PicPayDatabase
import com.picpay.desafio.android.utils.extensions.fromEntitiesToUsers
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
class SetFavoriteTest : TestCase() {

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
    private lateinit var setFavorite: SetFavorite

    @Before
    fun setup() {
        hiltRule.inject()

        dao = db.favoriteDao()

        val repository = LocalRepository(dao)
        setFavorite = SetFavorite(repository)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun shouldPassWhenInsertingFavorite(): Unit = runBlocking {
        user.favorite = true
        setFavorite.invoke(SetFavorite.Params(user))
        val favorites = dao.getFavorites()

        assertThat(favorites?.fromEntitiesToUsers()).contains(user)
    }

    @Test
    fun shouldPassWhenDeletingFavorite(): Unit = runBlocking {
        user.favorite = false
        setFavorite.invoke(SetFavorite.Params(user))
        val favorites = dao.getFavorites()

        assertThat(favorites?.fromEntitiesToUsers()).doesNotContain(user)
    }
}
