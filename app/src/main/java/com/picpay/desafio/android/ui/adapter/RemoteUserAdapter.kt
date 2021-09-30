package com.picpay.desafio.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.bg
import com.picpay.desafio.android.utils.extensions.set

class RemoteUserAdapter(
    private val onClickListener: (view: View, user: User, position: Int) -> Unit
) : PagingDataAdapter<User, RemoteUserAdapter.ViewHolder>(RemoteUserAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = AdapterItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.viewBiding(it) }
    }

    inner class ViewHolder(
        private val biding: AdapterItemBinding,
        private val onClickListener: (view: View, user: User, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(user: User) {
            biding.apply {
                textUsername.text(user.username)
                textName.text = user.name
                imageUser.bg()
                textFirstChar.text = user.name.first().toString()
                imageUser.set(user.img) { textFirstChar.text = "" }
            }

            itemView.setOnClickListener {
                it.postDelayed({
                    onClickListener.invoke(it, user, layoutPosition)
                }, 300)
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
