package com.picpay.desafio.android.utils.extensions

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.model.UserDomain

fun UserDomain.fromUserDomainToUser(): User {
    return User(
        id = this.id,
        username = this.username,
        name = this.name,
        img = this.img,
        favorite = false
    )
}

fun List<UserDomain>.fromDomainsToPhotos(): List<User> {
    return this.map { it.fromUserDomainToUser() }
}
