package com.picpay.desafio.android.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.LocalRepository
import com.picpay.desafio.android.data.room.FavoriteDao
import com.picpay.desafio.android.data.room.PicPayDatabase
import com.picpay.desafio.android.utils.extensions.fromEntitiesToUsers
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SetFavoriteTest : TestCase() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var db: PicPayDatabase
    private lateinit var dao: FavoriteDao
    private lateinit var setFavorite: SetFavorite
    private val user = User(
        "1",
        "Sandrine Spinka",
        "https://randomuser.me/api/portraits/men/1.jpg",
        "Tod86"
    )

    @Before
    public override fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PicPayDatabase::class.java
        ).allowMainThreadQueries().build()
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
