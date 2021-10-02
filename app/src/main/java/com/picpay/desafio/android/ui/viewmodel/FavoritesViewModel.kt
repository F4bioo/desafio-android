package com.picpay.desafio.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.api.DataState
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.usecase.GetFavorites
import com.picpay.desafio.android.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val getFavorites: GetFavorites,
    private val setFavorite: SetFavorite
) : ViewModel() {
    private val _getFavoritesEvent = MutableLiveData<DataState<List<User>?>>()
    val getFavoritesEvent: LiveData<DataState<List<User>?>>
        get() = _getFavoritesEvent

    private val _setFavoritesEvent = MutableLiveData<DataState<User?>>()
    val setFavoritesEvent: LiveData<DataState<User?>>
        get() = _setFavoritesEvent

    fun getFavorites() {
        viewModelScope.launch {
            _getFavoritesEvent.value = getFavorites.invoke()
        }
    }

    fun setFavorite(user: User) {
        viewModelScope.launch {
            _setFavoritesEvent.value = setFavorite.invoke(SetFavorite.Params(user))
        }
    }
}
