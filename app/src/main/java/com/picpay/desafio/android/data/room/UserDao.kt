package com.picpay.desafio.android.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picpay.desafio.android.data.model.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllUsers(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getAllUsers(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM users")
    suspend fun delAllUsers()
}
