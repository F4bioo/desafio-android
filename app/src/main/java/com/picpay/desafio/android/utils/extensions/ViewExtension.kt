package com.picpay.desafio.android.utils.extensions

import android.graphics.Color
import android.widget.ImageView
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
                onCallBack()
            }

            override fun onError(e: Exception?) {
                onCallBack()
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
