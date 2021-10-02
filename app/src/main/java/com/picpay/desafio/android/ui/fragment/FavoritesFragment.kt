package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.databinding.FragmentFavoritesBinding
import com.picpay.desafio.android.ui.adapter.LocalUserAdapter
import com.picpay.desafio.android.ui.viewmodel.FavoritesViewModel
import com.picpay.desafio.android.utils.extensions.safelyNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoritesViewModel>()

    private val adapter by lazy {
        LocalUserAdapter { view, user ->
            when (view.id) {
                R.id.check_favorite -> {
                    viewModel.setFavorite(user)
                }
                else -> {
                    val directions =
                        FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(user)
                    findNavController().safelyNavigate(directions)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
        viewBiding()
        getFavorites()
        initListeners()
        initObserver()
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun viewBiding() {
        binding.includeHeader.apply {
            textTitle.text = getString(R.string.favorites)
            radioGroup.isVisible = false
            buttonArrow.isVisible = true
        }
        binding.includeEmpty.apply {
            textEmpty.text = getString(R.string.favorites_empty_list)
            binding.includeEmpty.buttonRetry.isVisible = false
            binding.includeEmpty.progressEmpty.isVisible = false
        }
    }

    private fun initListeners() {
        binding.includeHeader.buttonArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.includeList.refresh.setOnRefreshListener {
            getFavorites()
        }
    }

    private fun initObserver() {
        viewModel.getFavoritesEvent.observe(viewLifecycleOwner) { dataState ->
            binding.includeList.refresh.isRefreshing = false

            if (dataState is DataState.OnSuccess) {
                val users = dataState.data?.toMutableList()
                adapter.clearList()
                adapter.submitList(users)
                emptyLayout()
            }
        }

        viewModel.setFavoritesEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess) {
                view?.postDelayed({
                    getFavorites()
                }, 500)
            }
        }
    }

    private fun initRecyclerView() {
        binding.includeList.apply {
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.setHasFixedSize(true)
            recycler.adapter = adapter
        }
    }

    private fun emptyLayout() {
        binding.includeEmpty.root.isVisible =
            adapter.itemCount == 0
    }

    private fun getFavorites() {
        viewModel.getFavorites()
    }
}
