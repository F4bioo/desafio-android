package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.*
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
import com.picpay.desafio.android.utils.SharedViewModel
import com.picpay.desafio.android.utils.extensions.safelyNavigate
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
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var isOnTop = true
    private var pos = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        viewBiding()
        initObserver()
        initRecyclerView()
        initListeners()
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
            when {
                dataState is DataState.OnSuccess && dataState.data == null
                        || dataState is DataState.OnException -> Toast.makeText(
                    requireContext(), getString(R.string.generic_error), Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.getPrefsDayNightMode { isDayMode ->
            if (isDayMode) {
                binding.includeHeader.radioDayMode.isChecked = true
            } else binding.includeHeader.radioNightMode.isChecked = true
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

            when (view.id) {
                R.id.check_favorite -> viewModel.setFavorite(user)
                else -> findNavController().safelyNavigate(
                    MainFragmentDirections.actionMainFragmentToDetailsFragment(user)
                )
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

        binding.includeHeader.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_day_mode -> viewModel.setPrefsDayNightMode(true) {
                    sharedViewModel.setResult(true)
                }

                else -> viewModel.setPrefsDayNightMode(false) {
                    sharedViewModel.setResult(false)
                }
            }
        }

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
