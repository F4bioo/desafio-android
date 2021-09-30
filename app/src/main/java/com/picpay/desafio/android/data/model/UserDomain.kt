package com.picpay.desafio.android.data.model

import com.google.gson.annotations.SerializedName

data class UserDomain(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("username")
    val username: String
)
