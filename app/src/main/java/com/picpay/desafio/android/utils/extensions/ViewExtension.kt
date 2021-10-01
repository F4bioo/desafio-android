package com.picpay.desafio.android.utils.extensions

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.picpay.desafio.android.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

fun ImageView.set(imageUrl: String, onCallBack: () -> Unit) {
    Picasso.get()
        .load(imageUrl)
        .noPlaceholder()
        .noFade()
        .into(this, object : Callback {
            override fun onSuccess() {
                onCallBack.invoke()
            }

            override fun onError(e: Exception?) {
            }
        })
}

fun ImageView.bg() {
    Random().apply {
        val color = Color.argb(
            125,
            nextInt(256),
            nextInt(256),
            nextInt(256)
        )
        setBackgroundColor(color)
    }
}

fun Boolean.setDayNightMode() {
    AppCompatDelegate.setDefaultNightMode(
        if (this) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
    )
}

fun Window.setStatusBarColor() {
    statusBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.color(R.color.header_main)
    } else context.color(R.color.black)
}

fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}
