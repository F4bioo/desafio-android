package com.picpay.desafio.android.utils.extensions

import android.view.View
import com.picpay.desafio.android.data.model.User

typealias ItemClicked = (view: View, user: User, position: Int) -> Unit
