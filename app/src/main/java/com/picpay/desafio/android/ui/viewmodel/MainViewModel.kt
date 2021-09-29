package com.picpay.desafio.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.GetUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getUsers: GetUsers
) : ViewModel() {
    private val _getUsersEvent = MutableLiveData<DataState<List<User>>?>()
    val getUsersEvent: LiveData<DataState<List<User>>?>
        get() = _getUsersEvent

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            _getUsersEvent.value = getUsers.invoke()
        }
    }
}
