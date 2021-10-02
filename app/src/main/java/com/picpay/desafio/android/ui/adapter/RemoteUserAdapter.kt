package com.picpay.desafio.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.GetFavorite
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.bg
import com.picpay.desafio.android.utils.extensions.set
import com.picpay.desafio.android.utils.extensions.username
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RemoteUserAdapter(
    private val lifecycle: Lifecycle,
    private val getFavorite: GetFavorite,
    private val onClickListener: (view: View, user: User) -> Unit
) : PagingDataAdapter<User, RemoteUserAdapter.ViewHolder>(RemoteUserAdapter) {

    private var job: Job? = null

    private companion object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

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
        private val onClickListener: (view: View, user: User) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(user: User) {
            biding.apply {
                textUsername.username(user.username)
                textName.text = user.name
                imageUser.bg()
                textFirstChar.text = user.name.first().toString()
                imageUser.set(user.img) { textFirstChar.text = "" }
                checkFavorite.isFavorite(user.id)

                checkFavorite.setOnClickListener {
                    user.favorite = checkFavorite.isChecked
                    onClickListener.invoke(it, user)
                }
            }

            itemView.setOnClickListener {
                it.postDelayed({
                    user.favorite = biding.checkFavorite.isChecked
                    onClickListener.invoke(it, user)
                }, 300)
            }
        }
    }

    private fun CheckBox.isFavorite(id: String) {
        job = lifecycle.coroutineScope.launch {
            val dataState = getFavorite.invoke(GetFavorite.Params(id))
            isChecked = dataState is DataState.OnSuccess
                    && dataState.data == true
        }
    }

    fun jobCancel() {
        job?.cancel()
    }
}
