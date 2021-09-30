package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.FragmentMainBinding
import com.picpay.desafio.android.ui.adapter.RemoteUserAdapter
import com.picpay.desafio.android.ui.adapter.paging.UserLoadState
import com.picpay.desafio.android.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalPagingApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private var auxHasReachedTop = true

    private val adapter by lazy {
        RemoteUserAdapter { view, user, position ->

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        binding.progressMain.isVisible = true
        initObserver()
        initRecyclerView()
        initListeners()
        handleOnBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.pagingEvent.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
            binding.progressMain.isVisible = false
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerMain.layoutManager = LinearLayoutManager(requireContext())
            recyclerMain.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UserLoadState { adapter.retry() },
                footer = UserLoadState { adapter.retry() }
            )
        }
    }

    private fun initListeners() {
        binding.recyclerMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                auxHasReachedTop = !recyclerView.canScrollVertically(-1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE
            }
        })

        binding.groupTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_light_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    recreate(requireActivity())
                }
                R.id.radio_dark_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    recreate(requireActivity())
                }
            }
        }
    }

    private fun handleOnBackPressed() {
        val fa = requireActivity()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (auxHasReachedTop) {
                    fa.finish()
                } else binding.recyclerMain.smoothScrollToPosition(0)
            }
        }
        fa.onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, callback)
    }
}
