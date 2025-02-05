package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.FragmentDetailsBinding
import com.picpay.desafio.android.ui.viewmodel.DetailsViewModel
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailsViewModel>()
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.detailsArgs?.let {
            user = it
            viewBiding()
            initListeners()
            initObserver()
        } ?: findNavController().popBackStack()
    }

    private fun viewBiding() {
        binding.apply {
            textUsername.username(user.username)
            textName.name(user.name)
            imageUser.bg()
            textFirstChar.charAt(user.name)
            imageUser.set(user.img) { textFirstChar.text = "" }
            if (user.favorite)
                buttonFavorite.favorite(
                    R.drawable.ic_baseline_favorite_selected,
                    R.color.color_favorite_selected
                )
        }
    }

    private fun initListeners() {
        binding.apply {
            buttonShare.setOnClickListener {
                viewModel.shareContact(
                    getString(R.string.share_extra, user.username, user.name),
                    getString(R.string.share_title)
                ) { startActivity(this) }
            }

            buttonFavorite.setOnClickListener {
                user.favorite = when (user.favorite) {
                    true -> buttonFavorite.favorite(
                        R.drawable.ic_baseline_favorite_unselected,
                        R.color.color_icon_modal
                    ).let { false }

                    else -> buttonFavorite.favorite(
                        R.drawable.ic_baseline_favorite_selected,
                        R.color.color_favorite_selected
                    ).let { true }
                }
                viewModel.setFavorite(user)
            }
        }
    }

    private fun initObserver() {
        viewModel.setFavoriteEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess && dataState.data == null
                || dataState is DataState.OnException
            ) requireContext().errorToast()
            else setFragmentResult(Constants.KEY_FAVORITE, bundleOf())
        }
    }

    fun MaterialButton.favorite(@DrawableRes icon: Int, @ColorRes color: Int) {
        setIconResource(icon)
        setIconTintResource(color)
    }
}
