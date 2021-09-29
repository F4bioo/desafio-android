package com.picpay.desafio.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val username: String,
    val name: String,
    val img: String,
    val favorite: Boolean
) : Parcelable
