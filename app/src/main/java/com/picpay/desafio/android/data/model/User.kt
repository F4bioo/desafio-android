package com.picpay.desafio.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val img: String,
    val username: String,
    val favorite: Boolean = false
) : Parcelable
