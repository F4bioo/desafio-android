package com.picpay.desafio.android.ui.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    private val setFavorite: SetFavorite
) : ViewModel() {

    private val _setFavoriteEvent = MutableLiveData<DataState<User?>>()
    val setFavoriteEvent: LiveData<DataState<User?>>
        get() = _setFavoriteEvent

    fun setFavorite(user: User) {
        viewModelScope.launch {
            _setFavoriteEvent.value = setFavorite.invoke(SetFavorite.Params(user))
        }
    }

    fun shareContact(
        textExtra: String,
        textTitle: String,
        startActivity: Intent.() -> Unit
    ) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                textExtra.trimMargin()
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, textTitle)
        startActivity.invoke(shareIntent)
    }
}
