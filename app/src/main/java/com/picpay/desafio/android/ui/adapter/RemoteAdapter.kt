package com.picpay.desafio.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.set

class RemoteUserAdapter(
) : PagingDataAdapter<User, RemoteUserAdapter.ViewHolder>(RemoteUserAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = AdapterItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.viewBiding(it) }
    }

    inner class ViewHolder(
        private val biding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(user: User) {
            biding.apply {
                textUsername.text(user.username)
                textName.text = user.name
                progressCircular.apply { isVisible = true }.apply {
                    imageUser.set(user.img) { isVisible = it }
                }
            }
        }
    }

    private companion object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    fun TextView.text(username: String) {
        text = context.getString(R.string.username, username)
    }
}
