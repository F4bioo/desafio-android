package com.picpay.desafio.android.utils.extensions

import android.widget.ImageView
import com.picpay.desafio.android.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.set(imageUrl: String, onCallBack: (showProgress: Boolean) -> Unit) {
    Picasso.get()
        .load(imageUrl)
        .error(R.drawable.ic_round_account_circle)
        .into(this, object : Callback {
            override fun onSuccess() {
                onCallBack(false)
            }

            override fun onError(e: Exception?) {
                onCallBack(false)
            }
        })
}
