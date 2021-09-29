package com.picpay.desafio.android.data.model

import com.google.gson.annotations.SerializedName

data class UserDomain(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("img") val img: String
)
