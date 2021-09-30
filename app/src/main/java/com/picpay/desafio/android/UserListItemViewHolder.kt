package com.picpay.desafio.android

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.set

class UserListItemViewHolder(
    private val biding: AdapterItemBinding,
) : RecyclerView.ViewHolder(biding.root) {

    fun bind(user: User) {
        biding.apply {
            textUsername.text(user.username)
            textName.text = user.name
            progressCircular.visibility = View.VISIBLE
            imageUser.set(user.img) {
                progressCircular.isVisible = it
            }
        }
    }

    fun TextView.text(username: String) {
        text = context.getString(R.string.username, username)
    }
}
