package com.picpay.desafio.android.utils.extensions

import android.graphics.Color
import android.os.Build
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
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
                onCallBack.invoke()
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

fun Window.setStatusBarColor(isNightMode: Boolean) {
    val isApi23 = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    statusBarColor = if (isApi23) {
        if (isNightMode) Color.BLACK
        else Color.WHITE
    } else Color.BLACK
}
