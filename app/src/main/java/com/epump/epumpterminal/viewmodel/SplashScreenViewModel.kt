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

class SplashScreenViewModel @ViewModelInject constructor(
    private val repository: LoginRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userDetails : LiveData<User> = repository.userDetails
}