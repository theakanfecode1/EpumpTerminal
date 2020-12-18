package com.epump.epumpterminal.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.epump.epumpterminal.api.StationDataSource
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.Pump
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

class PumpViewModel @ViewModelInject constructor(
    private val repository: PumpRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState : MutableLiveData<Resource<List<Pump>>> = MutableLiveData()
    val dataState : LiveData<Resource<List<Pump>>>
        get() = _dataState


    init {
        getPumps(PumpFragment.branchId)
    }

    fun getPumps(branchId : String){
        viewModelScope.launch {
            repository.getPumps(branchId)
                .onEach {
                    dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
    fun setCheckedForPump(pumpId : String){
        val pumps  = _dataState.value?.data
        pumps?.forEach {
            if(it.id.equals(pumpId)){
                it.isChecked = true
            }
        }
        _dataState.value = Resource.success(pumps!!)
    }

}