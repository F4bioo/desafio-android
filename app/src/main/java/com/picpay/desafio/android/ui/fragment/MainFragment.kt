package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.databinding.FragmentMainBinding
import com.picpay.desafio.android.ui.adapter.RemoteUserAdapter
import com.picpay.desafio.android.ui.viewmodel.MainViewModel
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.extensions.errorToast
import com.picpay.desafio.android.utils.extensions.safelyNavigate
import com.picpay.desafio.android.utils.extensions.setDayNightMode
import com.picpay.desafio.android.utils.extensions.setOnBackPressedDispatcher
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalPagingApi
@AndroidEntryPoint
class MainFragment constructor(
    val adapter: RemoteUserAdapter
) : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private var isOnTop = true
    private var pos = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        viewBiding()
        initObserver()
        initListeners()
        initRecyclerView()
        requireActivity()
            .initOnBackDispatcher()
    }

    override fun onDestroyView() {
        adapter.jobCancel()
        super.onDestroyView()
    }

    private fun viewBiding() {
        binding.includeList.progress.isVisible = true
    }

    private fun initObserver() {
        viewModel.pagingEvent.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
            binding.includeList.progress.isVisible = false
            binding.includeList.refresh.isRefreshing = false
        }

        viewModel.setFavoriteEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess && dataState.data == null
                || dataState is DataState.OnException
            ) requireContext().errorToast()
        }

        viewModel.getDayNightEvent.observe(viewLifecycleOwner) { isDayMode ->
            isDayMode.setDayNightMode()

            binding.includeHeader.apply {
                when (isDayMode) {
                    true -> radioDayMode.isChecked = true
                    else -> radioNightMode.isChecked = true
                }
            }
        }

        setFragmentResultListener(Constants.KEY_FAVORITE) { _, _ ->
            adapter.notifyItemChanged(pos)
        }
    }

    private fun initRecyclerView() {
        binding.includeList.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.includeList.recycler.adapter = adapter
    }

    private fun initListeners() {
        adapter.setOnItemClickListener { view, user, position ->
            pos = position

            when {
                view.id == R.id.check_favorite -> viewModel.setFavorite(user)
                user.username.isEmpty() || user.name.isEmpty() -> requireContext().errorToast()
                else -> findNavController().safelyNavigate(
                    MainFragmentDirections.actionMainFragmentToDetailsFragment(user)
                )
            }
        }

        binding.includeHeader.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group[0].isPressed || group[1].isPressed) {
                viewModel.storeDayNightValue(checkedId == R.id.radio_day_mode)
            }
        }

        binding.includeList.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING)
                    if (dy > 0) binding.fab.shrink()
                    else if (dy < 0) binding.fab.extend()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isOnTop = !recyclerView.canScrollVertically(-1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE

                binding.fab.isVisible = !(!recyclerView.canScrollVertically(1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE)
            }
        })

        adapter.addLoadStateListener { loadStates ->
            loadStates.emptyListRule()
        }

        binding.includeEmpty.buttonRetry.setOnClickListener {
            viewModel.getUsersFromMediator()
        }

        binding.fab.setOnClickListener {
            findNavController().safelyNavigate(
                MainFragmentDirections.actionMainFragmentToFavoritesFragment()
            )
        }

        binding.includeList.refresh.setOnRefreshListener {
            viewModel.getUsersFromMediator()
        }
    }

    private fun CombinedLoadStates.emptyListRule() {
        binding.includeEmpty.apply {
            val isLoading = refresh is LoadState.Loading
            val isError = refresh is LoadState.Error

            emptyLayout.isVisible = (isLoading || isError)
                    && adapter.itemCount == 0
            progressEmpty.isVisible = isLoading
            buttonRetry.isVisible = isError

            textEmpty.text = when {
                isLoading -> getString(R.string.loading_list)
                else -> getString(R.string.main_empty_list)
            }
        }
    }

    private fun FragmentActivity.initOnBackDispatcher() {
        setOnBackPressedDispatcher {
            if (isOnTop) finish()
            else binding.includeList.recycler
                .smoothScrollToPosition(0)
        }
    }
}
