package com.picpay.desafio.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.AdapterItemBinding
import com.picpay.desafio.android.utils.extensions.ItemClicked
import com.picpay.desafio.android.utils.extensions.bg
import com.picpay.desafio.android.utils.extensions.set
import com.picpay.desafio.android.utils.extensions.username
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
                textName.text = user.name
                imageUser.bg()
                textFirstChar.text = user.name.first().toString()
                imageUser.set(user.img) { textFirstChar.text = "" }
                checkFavorite.isChecked = user.favorite

                itemView.setOnClickListener {
                    it.postDelayed({
                        onClickListener?.invoke(it, user, layoutPosition)
                    }, 300)
                }

                checkFavorite.setOnClickListener {
                    user.favorite = checkFavorite.isChecked
                    modifyItemList(layoutPosition, user)
                    onClickListener?.invoke(it, user, layoutPosition)
                }
            }
        }
    }

    override fun submitList(list: MutableList<User>?) {
        super.submitList(list?.distinct())
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

    fun setOnItemClickListener(ItemClicked: ItemClicked) {
        onClickListener = ItemClicked
    }
}
