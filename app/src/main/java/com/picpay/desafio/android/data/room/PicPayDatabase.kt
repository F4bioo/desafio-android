package com.picpay.desafio.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.picpay.desafio.android.data.model.RemoteKeyEntity
import com.picpay.desafio.android.data.model.UserEntity

@Database(
    entities = [UserEntity::class, RemoteKeyEntity::class],
    version = 1
)
abstract class PicPayDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun keyDao(): RemoteKeyDao
}
