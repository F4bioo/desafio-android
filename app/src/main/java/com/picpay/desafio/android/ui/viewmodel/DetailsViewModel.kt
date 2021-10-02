package com.picpay.desafio.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.GetFavorite
import com.picpay.desafio.android.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModalViewModel
@Inject
constructor(
    private val getFavorite: GetFavorite,
    private val setFavorite: SetFavorite
) : ViewModel() {
    private val _insertEvent = MutableLiveData<DataState<Boolean>>()
    val insertEvent: LiveData<DataState<Boolean>>
        get() = _insertEvent

    private val _selectEvent = MutableLiveData<DataState<Boolean>>()
    val selectEvent: LiveData<DataState<Boolean>>
        get() = _selectEvent

    fun getFavorite(id: String) {
        viewModelScope.launch {
            _selectEvent.value = getFavorite.invoke(GetFavorite.Params(id))
        }
    }

    fun setFavorite(user: User) {
        viewModelScope.launch {
            _insertEvent.value = setFavorite.invoke(SetFavorite.Params(user))
        }
    }
}
