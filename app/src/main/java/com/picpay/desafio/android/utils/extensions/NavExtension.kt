package com.picpay.desafio.android.utils.extensions

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safelyNavigate(@IdRes resId: Int) = try {
    navigate(resId, null)
} catch (e: Exception) {
}

fun NavController.safelyNavigate(directions: NavDirections) = try {
    navigate(directions)
} catch (e: Exception) {
}
