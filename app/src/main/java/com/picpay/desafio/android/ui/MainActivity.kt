package com.picpay.desafio.android.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.picpay.desafio.android.R
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.Prefs
import com.picpay.desafio.android.utils.SharedViewModel
import com.picpay.desafio.android.utils.extensions.setDayNightMode
import com.picpay.desafio.android.utils.extensions.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var prefs: Prefs

    private val sharedViewModel by viewModels<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDayNightMode()
        initObserver()
    }

    private fun initDayNightMode() {
        val isDayMode = prefs.getBoolean(
            Constants.KEY_DAY_NIGHT_MODE, true
        )
        isDayMode.setDayNightMode()
        window.setStatusBarColor()
    }

    private fun initObserver() {
        sharedViewModel.getResult<Boolean>().observe(this) { isDayMode ->
            isDayMode.setDayNightMode()
        }
    }
}
