package com.picpay.desafio.android.utils.extensions

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.model.UserDomain
import com.picpay.desafio.android.data.model.UserEntity

fun UserDomain.fromUserDomainToUser(): User {
    return User(
        id = this.id,
        name = this.name,
        img = this.img,
        username = this.username
    )
}

fun UserEntity.fromUserEntityToUser(): User {
    return User(
        id = this.id,
        name = this.name,
        img = this.img,
        username = this.username
    )
}

fun User.fromUserToUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        img = this.img,
        username = this.username
    )
}

fun List<UserDomain>.fromDomainsToUsers(): List<User> {
    return this.map { it.fromUserDomainToUser() }
}

fun List<User>.fromDomainsToEntities(): List<UserEntity> {
    return this.map { it.fromUserToUserEntity() }
}
