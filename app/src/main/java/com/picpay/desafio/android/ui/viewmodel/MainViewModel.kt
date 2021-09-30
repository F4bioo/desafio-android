package com.picpay.desafio.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.GetMediatorData
import com.picpay.desafio.android.utils.extensions.fromUserEntityToUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getMediatorData: GetMediatorData
) : ViewModel() {
    private val _pagingEvent = MutableLiveData<PagingData<User>>()
    val pagingEvent: LiveData<PagingData<User>>
        get() = _pagingEvent

    private val _getUsersEvent = MutableLiveData<DataState<List<User>>?>()
    val getUsersEvent: LiveData<DataState<List<User>>?>
        get() = _getUsersEvent

    init {
        getPhotosFromMediator()
    }

    fun getPhotosFromMediator() {
        viewModelScope.launch {
            getMediatorData.invoke().cachedIn(viewModelScope).collect {
                _pagingEvent.value = it.map { entity ->
                    entity.fromUserEntityToUser()
                }
            }
        }
    }
}
