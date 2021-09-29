package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.UserListAdapter
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.databinding.FragmentMainBinding
import com.picpay.desafio.android.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.adapter.rxjava2.Result.response

import org.json.JSONObject
import java.lang.Exception


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy {
        UserListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        binding.progressMain.isVisible = true
        initObserver()
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.getUsersEvent.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.OnSuccess -> {
                    println("<> OnSuccess ${dataState.data.size}")
                    binding.progressMain.isVisible = false

                    adapter.users = dataState.data
                }
                is DataState.OnError -> {
                    println("<> OnError ${dataState.errorBody}")
                }
                is DataState.OnException -> {
                    binding.progressMain.isVisible = false
                    binding.recyclerMain.isVisible = false

                    println("<> OnException ${dataState.e.message}")
                    val message = getString(R.string.error)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerMain.layoutManager = LinearLayoutManager(requireContext())
            recyclerMain.adapter = adapter
        }
    }
}
