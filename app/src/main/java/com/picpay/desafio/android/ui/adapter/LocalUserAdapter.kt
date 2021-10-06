package com.picpay.desafio.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.*
import javax.inject.Inject

class LocalUserAdapter
@Inject constructor(
) : ListAdapter<User, LocalUserAdapter.ViewHolder>(LocalUserAdapter) {

    private val users = arrayListOf<User>()
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
        val user = users[position]
        holder.viewBiding(user)
    }

    override fun getItemCount() = users.size

    inner class ViewHolder(
        private val biding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(user: User) {
            biding.apply {
                textUsername.username(user.username)
                textName.name(user.name)
                imageUser.bg()
                textFirstChar.charAt(user.name)
                imageUser.set(user.img) { textFirstChar.text = "" }
                checkFavorite.isChecked = user.favorite

                checkFavorite.setOnClickListener {
                    biding.clicked(it, user, layoutPosition)
                }
            }

            itemView.setOnClickListener {
                it.postDelayed({
                    biding.clicked(it, user, layoutPosition)
                }, 300)
            }
        }
    }

    override fun submitList(list: MutableList<User>?) {
        super.submitList(list)
        list?.let {
            users.addAll(it)
        }
    }

    fun clearList() {
        users.clear()
    }

    fun modifyItemList(position: Int, user: User) {
        users[position] = user
        notifyItemChanged(position)
    }

    private fun AdapterItemBinding.clicked(view: View, user: User, position: Int) {
        user.favorite = checkFavorite.isChecked
        onClickListener?.invoke(view, user, position)
    }

    fun setOnItemClickListener(ItemClicked: ItemClicked) {
        onClickListener = ItemClicked
    }
}
