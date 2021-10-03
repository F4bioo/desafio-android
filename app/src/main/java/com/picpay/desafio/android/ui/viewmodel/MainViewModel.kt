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
import com.picpay.desafio.android.data.usecase.SetFavorite
import com.picpay.desafio.android.utils.Constants
import com.picpay.desafio.android.utils.Prefs
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
    private val setFavorite: SetFavorite,
    private val getMediatorData: GetMediatorData,
    private val prefs: Prefs
) : ViewModel() {
    private val _pagingEvent = MutableLiveData<PagingData<User>>()
    val pagingEvent: LiveData<PagingData<User>>
        get() = _pagingEvent

    private val _setFavoriteEvent = MutableLiveData<DataState<User?>>()
    val setFavoriteEvent: LiveData<DataState<User?>>
        get() = _setFavoriteEvent

    init {
        getUsersFromMediator()
    }

    fun getUsersFromMediator() {
        viewModelScope.launch {
            getMediatorData.invoke().cachedIn(viewModelScope).collect {
                _pagingEvent.value = it.map { entity ->
                    entity.fromUserEntityToUser()
                }
            }
        }
    }

    fun getPrefsDayNightMode(notify: (isDayMode: Boolean) -> Unit) {
        val isDayMode = prefs.getBoolean(
            Constants.KEY_DAY_NIGHT_MODE, true
        )
        notify.invoke(isDayMode)
    }

    fun setPrefsDayNightMode(isDayMode: Boolean, notify: () -> Unit) {
        prefs.setBoolean(Constants.KEY_DAY_NIGHT_MODE, isDayMode)
        notify.invoke()
    }

    fun setFavorite(user: User) {
        viewModelScope.launch {
            _setFavoriteEvent.value = setFavorite.invoke(SetFavorite.Params(user))
        }
    }
}
