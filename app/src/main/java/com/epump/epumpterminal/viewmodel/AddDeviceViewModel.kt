package com.epump.epumpterminal.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.epump.epumpterminal.models.Device
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.models.Tank
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.repository.PumpRepository
import com.epump.epumpterminal.repository.StationRepository
import com.epump.epumpterminal.repository.TankRepository
import com.epump.epumpterminal.ui.PumpFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddDeviceViewModel @ViewModelInject constructor(
    private val pumpRepository: PumpRepository,
    private val tankRepository: TankRepository,
    private val stationRepository: StationRepository,

    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState : MutableLiveData<Resource<List<Pump>>> = MutableLiveData()
    val dataState : LiveData<Resource<List<Pump>>>
        get() = _dataState

    private val _tankDataState: MutableLiveData<Resource<List<Tank>>> = MutableLiveData()
    val tankDataState : LiveData<Resource<List<Tank>>>
        get() = _tankDataState

    private val _addDeviceDataState: MutableLiveData<Resource<Void?>> = MutableLiveData()
    val addDeviceDataState : LiveData<Resource<Void?>>
        get() = _addDeviceDataState


    init {
        getPumps(PumpFragment.branchId)
        getTanks(PumpFragment.branchId)
    }

    fun getPumps(branchId : String){
        viewModelScope.launch {
            pumpRepository.getPumps(branchId)
                .onEach {
                    dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
    fun getTanks(branchId : String){
        viewModelScope.launch {
            tankRepository.getTanks(branchId)
                .onEach {
                        dataState ->
                    _tankDataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

    fun addDevice(device:Device){
        viewModelScope.launch {
            stationRepository.addDevice(device)
                .onEach {
                        dataState ->
                    _addDeviceDataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }




}