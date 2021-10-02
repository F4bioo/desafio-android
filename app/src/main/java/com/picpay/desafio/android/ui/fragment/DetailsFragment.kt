package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.FragmentDetailsBinding
import com.picpay.desafio.android.utils.extensions.bg
import com.picpay.desafio.android.utils.extensions.set
import com.picpay.desafio.android.utils.extensions.username


class DetailsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
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
        user = args.detailsArgs
        viewBiding()
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun viewBiding() {
        binding.apply {
            textUsername.username(user.username)
            textName.text = user.name
            imageUser.bg()
            textFirstChar.text = user.name.first().toString()
            imageUser.set(user.img) { textFirstChar.text = "" }
            if (user.favorite) buttonFavorite
                .favorite(R.drawable.ic_baseline_favorite_selected, R.color.color_favorite)
        }
    }

    private fun initListeners() {
        binding.apply {
            buttonShare.setOnClickListener {

            }

            buttonFavorite.setOnClickListener {
                user.favorite = when (user.favorite) {
                    true -> buttonFavorite
                        .favorite(R.drawable.ic_baseline_favorite_unselected, R.color.black)
                        .let { false }
                    else -> buttonFavorite
                        .favorite(R.drawable.ic_baseline_favorite_selected, R.color.color_favorite)
                        .let { true }
                }
            }
        }
    }

    fun MaterialButton.favorite(@DrawableRes icon: Int, @ColorRes color: Int) {
        setIconResource(icon)
        setIconTintResource(color)
    }
}
