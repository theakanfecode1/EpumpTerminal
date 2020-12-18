package com.epump.epumpterminal.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.epump.epumpterminal.api.StationDataSource
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.models.PumpToSend
import com.epump.epumpterminal.models.Station
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.repository.LoginRepository
import com.epump.epumpterminal.repository.PumpRepository
import com.epump.epumpterminal.repository.StationRepository
import com.epump.epumpterminal.ui.PumpFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PumpDetailsViewModel @ViewModelInject constructor(
    private val pumpRepository: PumpRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val pumps : MutableList<Pump> = mutableListOf()
    private val _dataState : MutableLiveData<MutableList<Pump>> = MutableLiveData()
    val dataState : LiveData<MutableList<Pump>>
        get() = _dataState


    fun addPump(pump: PumpToSend) = viewModelScope.launch {
        pumpRepository.insertPump(pump)
    }

}