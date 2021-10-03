package com.picpay.desafio.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.picpay.desafio.android.R
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.Prefs
import com.picpay.desafio.android.utils.SharedViewModel
import com.picpay.desafio.android.utils.extensions.ViewExtension.setDayNightMode
import com.picpay.desafio.android.utils.extensions.ViewExtension.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var prefs: Prefs

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
        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.getResult<Boolean>().observe(this) { isDayMode ->
            isDayMode.setDayNightMode()
        }
    }
}
