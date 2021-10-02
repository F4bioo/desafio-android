package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.usecase.GetFavorite
import com.picpay.desafio.android.databinding.FragmentMainBinding
import com.picpay.desafio.android.ui.adapter.RemoteUserAdapter
import com.picpay.desafio.android.ui.adapter.paging.UserLoadState
import com.picpay.desafio.android.ui.viewmodel.MainViewModel
import com.picpay.desafio.android.utils.SharedViewModel
import com.picpay.desafio.android.utils.extensions.navigateWithAnimations
import com.picpay.desafio.android.utils.extensions.safelyNavigate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var inOnTop = true

    @Inject
    lateinit var getFavorite: GetFavorite

    private val adapter by lazy {
        RemoteUserAdapter(lifecycle, getFavorite) { view, user ->
            when (view.id) {
                R.id.check_favorite -> {
                    viewModel.setFavorite(user)
                }
                else -> {
                    val directions =
                        MainFragmentDirections.actionMainFragmentToDetailsFragment(user)
                    findNavController().safelyNavigate(directions)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        viewBiding()
        initObserver()
        initRecyclerView()
        initListeners()
        handleOnBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.jobCancel()
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

        // TODO tratar erro ao inserir um favorito no banco (dica se user for null)

        viewModel.getPrefsDayNightMode { isNightMode ->
            if (isNightMode) {
                binding.includeHeader.radioNightMode.isChecked = true
            } else binding.includeHeader.radioDayMode.isChecked = true
        }
    }

    private fun initRecyclerView() {
        binding.includeList.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.includeList.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = UserLoadState { adapter.retry() },
            footer = UserLoadState { adapter.retry() }
        )
    }

    private fun initListeners() {
        binding.includeList.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING)
                    if (dy > 0) binding.fab.shrink()
                    else if (dy < 0) binding.fab.extend()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                inOnTop = !recyclerView.canScrollVertically(-1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE

                binding.fab.isVisible = !(!recyclerView.canScrollVertically(1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE)
            }
        })

        binding.includeHeader.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_night_mode -> viewModel.setPrefsDayNightMode(true) {
                    sharedViewModel.setResult(true)
                }

                else -> viewModel.setPrefsDayNightMode(false) {
                    sharedViewModel.setResult(false)
                }
            }
        }

        adapter.addLoadStateListener { loadStates ->
            loadStates.showEmptyList()
        }

        binding.includeEmpty.buttonRetry.setOnClickListener {
            adapter.retry()
        }

        binding.fab.setOnClickListener {
            findNavController()
                .navigateWithAnimations(R.id.action_mainFragment_to_favoritesFragment)
        }

        binding.includeList.refresh.setOnRefreshListener {
            viewModel.getUsersFromMediator()
        }
    }

    private fun CombinedLoadStates.showEmptyList() {
        binding.includeEmpty.apply {
            val isLoading = refresh is LoadState.Loading
            val isError = refresh is LoadState.Error

            root.isVisible = ((isLoading || isError) && adapter.itemCount == 0)
            buttonRetry.isVisible = isLoading.apply {
                progressEmpty.isVisible = !this
            }
            buttonRetry.isVisible = isError.apply {
                progressEmpty.isVisible = !this
            }

            textEmpty.text = when {
                isLoading -> getString(R.string.loading_list)
                else -> getString(R.string.main_empty_list)
            }
        }
    }

    private fun handleOnBackPressed() {
        val fa = requireActivity()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (inOnTop) {
                    fa.finish()
                } else binding.includeList.recycler
                    .smoothScrollToPosition(0)
            }
        }
        fa.onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, callback)
    }
}
