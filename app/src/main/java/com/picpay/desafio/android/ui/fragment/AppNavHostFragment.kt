package com.picpay.desafio.android.ui.fragment

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPagingApi
@AndroidEntryPoint
class AppNavHostFragment : NavHostFragment() {

    @Inject
    lateinit var appFragmentFactory: AppFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = appFragmentFactory
    }
}
