package com.picpay.desafio.android.ui.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.AdapterLoadBinding

class UserLoadState(
    private val onClickListener: () -> Unit
) : LoadStateAdapter<UserLoadState.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val biding = AdapterLoadBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.viewBiding(loadState)
    }

    inner class ViewHolder(
        private val binding: AdapterLoadBinding,
        private val onClickListener: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener { onClickListener.invoke() }
        }

        fun viewBiding(loadState: LoadState) {
            binding.apply {
                progressLoad.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState is LoadState.Error
                textError.isVisible = loadState is LoadState.Error
                textError.setText(R.string.pagination_error)
            }
        }
    }
}
