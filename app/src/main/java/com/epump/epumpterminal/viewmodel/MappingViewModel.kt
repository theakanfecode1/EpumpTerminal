package com.epump.epumpterminal.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.repository.LoginRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MappingViewModel @ViewModelInject constructor(
    private val repository: LoginRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState : MutableLiveData<Resource<User>> = MutableLiveData()
    val dataState : LiveData<Resource<User>>
        get() = _dataState

    fun deleteAllUser() = viewModelScope.launch {
        repository.deleteAllUser()
    }


}