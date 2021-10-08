package com.picpay.desafio.android.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _resultEvent = MutableLiveData<Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getResult(): LiveData<T> {
        return _resultEvent as MutableLiveData<T>
    }

    fun <T : Any> setResult(type: T) {
        _resultEvent.value = type
    }
}
