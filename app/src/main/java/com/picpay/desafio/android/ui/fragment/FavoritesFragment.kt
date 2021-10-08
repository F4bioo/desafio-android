package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.databinding.FragmentFavoritesBinding
import com.picpay.desafio.android.ui.adapter.LocalUserAdapter
import com.picpay.desafio.android.ui.viewmodel.FavoritesViewModel
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.extensions.errorToast
import com.picpay.desafio.android.utils.extensions.safelyNavigate
import com.picpay.desafio.android.utils.extensions.setOnBackPressedDispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment
@Inject constructor(
    var adapter: LocalUserAdapter
) : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoritesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
        viewBiding()
        getFavorites()
        initListeners()
        initObserver()
        initRecyclerView()
        requireActivity()
            .initOnBackDispatcher()
    }

    private fun viewBiding() {
        binding.includeHeader.apply {
            textTitle.text = getString(R.string.favorites_title)
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
        adapter.setOnItemClickListener { view, user, _ ->
            when (view.id) {
                R.id.check_favorite -> viewModel.setFavorite(user)
                else -> findNavController().safelyNavigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(user)
                )
            }
        }

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
            if (dataState is DataState.OnSuccess && dataState.data == null
                || dataState is DataState.OnException
            ) requireContext().errorToast()
            else view?.postDelayed({ getFavorites() }, 500)
        }

        setFragmentResultListener(Constants.KEY_FAVORITE) { _, _ ->
            getFavorites()
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
        binding.includeEmpty.emptyLayout.isVisible =
            adapter.itemCount == 0
    }

    private fun getFavorites() {
        viewModel.getFavorites()
    }

    private fun FragmentActivity.initOnBackDispatcher() {
        setOnBackPressedDispatcher {
            findNavController().popBackStack()
        }
    }
}
