package com.picpay.desafio.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.GetFavorite
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.ItemClicked
import com.picpay.desafio.android.utils.extensions.bg
import com.picpay.desafio.android.utils.extensions.set
import com.picpay.desafio.android.utils.extensions.username
import kotlinx.coroutines.*
import javax.inject.Inject

class RemoteUserAdapter @Inject
constructor(
    private val getFavorite: GetFavorite
) : PagingDataAdapter<User, RemoteUserAdapter.ViewHolder>(RemoteUserAdapter) {

    private var job: Job? = null
    private var onClickListener: ItemClicked? = null

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
                textUsername.username(user.username)
                textName.text = user.name
                imageUser.bg()
                textFirstChar.text = user.name.first().toString()
                imageUser.set(user.img) { textFirstChar.text = "" }
                checkFavorite.isFavorite(user.id)

                checkFavorite.setOnClickListener {
                    user.favorite = checkFavorite.isChecked
                    onClickListener?.invoke(it, user, layoutPosition)
                }
            }

            itemView.setOnClickListener {
                it.postDelayed({
                    user.favorite = biding.checkFavorite.isChecked
                    onClickListener?.invoke(it, user, layoutPosition)
                }, 300)
            }
        }
    }

    private fun CheckBox.isFavorite(id: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val dataState = getFavorite.invoke(GetFavorite.Params(id))
            withContext(Dispatchers.Main) {
                isChecked = dataState is DataState.OnSuccess
                        && dataState.data != null
            }
        }
    }

    fun jobCancel() {
        job?.cancel()
    }

    fun setOnItemClickListener(ItemClicked: ItemClicked) {
        onClickListener = ItemClicked
    }
}
