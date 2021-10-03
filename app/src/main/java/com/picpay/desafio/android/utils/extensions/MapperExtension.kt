package com.picpay.desafio.android.utils.extensions

import com.picpay.desafio.android.data.model.FavoriteEntity
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.model.UserDomain
import com.picpay.desafio.android.data.model.UserEntity

object MapperExtension {

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

    fun FavoriteEntity.fromUserEntityToUser(): User {
        return User(
            id = this.id,
            name = this.name,
            img = this.img,
            username = this.username,
            favorite = this.favorite
        )
    }

    fun User.fromUserToUserEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            img = this.img,
            username = this.username,
        )
    }

    fun User.fromUserToFavoriteEntity(): FavoriteEntity {
        return FavoriteEntity(
            id = this.id,
            name = this.name,
            img = this.img,
            username = this.username,
            favorite = this.favorite
        )
    }

    fun List<UserDomain>.fromDomainsToUsers(): List<User> {
        return this.map { it.fromUserDomainToUser() }
    }

    fun List<FavoriteEntity>.fromEntitiesToUsers(): List<User> {
        return this.map { it.fromUserEntityToUser() }
    }

    fun List<User>.fromDomainsToEntities(): List<UserEntity> {
        return this.map { it.fromUserToUserEntity() }
    }
}
