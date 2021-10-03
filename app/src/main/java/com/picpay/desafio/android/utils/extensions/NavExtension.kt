package com.picpay.desafio.android.utils.extensions

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.picpay.desafio.android.R

private val slideAnim = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

private fun NavController.safelyNavigate(@IdRes resId: Int) = try {
    navigate(resId, null, slideAnim)
} catch (e: Exception) {
}

private fun NavController.safelyNavigate(directions: NavDirections) = try {
    navigate(directions, slideAnim)
} catch (e: Exception) {
}

fun NavController.navigateWithAnimations(@IdRes resId: Int) {
    safelyNavigate(resId)
}

fun NavController.navigateWithAnimations(directions: NavDirections) {
    safelyNavigate(directions)
}
