package com.epump.epumpterminal.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.epump.epumpterminal.api.StationDataSource
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.Station
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.repository.LoginRepository
import com.epump.epumpterminal.repository.PumpRepository
import com.epump.epumpterminal.repository.StationRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class StationViewModel @ViewModelInject constructor(
    private val repository: StationRepository,
    private val pumpRepository: PumpRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {




    private val _dataState : MutableLiveData<Resource<List<Station>>> = MutableLiveData()
    val dataState : LiveData<Resource<List<Station>>>
        get() = _dataState

    init {
        getStations()
    }

    fun getStations(){
        viewModelScope.launch {
            repository.getStations()
                .onEach {
                    dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

     fun deletePumps() = viewModelScope.launch {
        pumpRepository.deletePump()
    }

}