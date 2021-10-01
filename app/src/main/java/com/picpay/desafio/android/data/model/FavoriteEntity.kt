package com.picpay.desafio.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "img")
    val img: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = true
)
