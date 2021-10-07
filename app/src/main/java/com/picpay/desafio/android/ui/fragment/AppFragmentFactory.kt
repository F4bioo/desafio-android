package com.picpay.desafio.android.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.paging.ExperimentalPagingApi
import com.picpay.desafio.android.ui.adapter.LocalUserAdapter
import com.picpay.desafio.android.ui.adapter.RemoteUserAdapter
import javax.inject.Inject

@ExperimentalPagingApi
class AppFragmentFactory
@Inject
constructor(
    private val remoteUserAdapter: RemoteUserAdapter,
    private val localUserAdapter: LocalUserAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            MainFragment::class.java.name -> MainFragment(remoteUserAdapter)
            FavoritesFragment::class.java.name -> FavoritesFragment(localUserAdapter)
            DetailsFragment::class.java.name -> DetailsFragment()
            else -> return super.instantiate(classLoader, className)
        }
    }
}
