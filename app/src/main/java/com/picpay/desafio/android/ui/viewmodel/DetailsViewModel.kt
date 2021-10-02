package com.picpay.desafio.android.ui.viewmodel

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
    private val _insertEvent = MutableLiveData<DataState<User?>>()
    val insertEvent: LiveData<DataState<User?>>
        get() = _insertEvent

    fun setFavorite(user: User) {
        viewModelScope.launch {
            _insertEvent.value = setFavorite.invoke(SetFavorite.Params(user))
        }
    }
}
