package com.picpay.desafio.android.utils.extensions

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.picpay.desafio.android.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

fun ImageView.set(imageUrl: String, onCallBack: () -> Unit) {
    val img = if (imageUrl.trim().isEmpty()) "."
    else imageUrl.trim()
    Picasso.get()
        .load(img)
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
        if (this) AppCompatDelegate.MODE_NIGHT_NO
        else AppCompatDelegate.MODE_NIGHT_YES
    )
}

fun Window.setStatusBarColor() {
    statusBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.color(R.color.color_header)
    } else context.color(R.color.black)
}

fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.errorToast() {
    Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show()
}

fun TextView.charAt(name: String) {
    text = if (name.trim().isNotEmpty()) {
        name.trim().first().toString()
    } else ""
}

fun TextView.username(username: String) {
    text = if (username.trim().isEmpty()) {
        context.getString(R.string.error_loading)
    } else context.getString(R.string.username, username.trim())
}

fun TextView.name(name: String) {
    text = if (name.trim().isEmpty()) {
        context.getString(R.string.check_connection)
    } else name.trim()
}

fun FragmentActivity.setOnBackPressedDispatcher(handleOnBackPressed: () -> Unit) {
    object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleOnBackPressed.invoke()
        }
    }.let { onBackPressedDispatcher.addCallback(it) }
}
