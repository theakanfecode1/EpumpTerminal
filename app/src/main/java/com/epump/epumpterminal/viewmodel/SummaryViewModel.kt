package com.epump.epumpterminal.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.epump.epumpterminal.api.StationDataSource
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.*
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.repository.LoginRepository
import com.epump.epumpterminal.repository.PumpRepository
import com.epump.epumpterminal.repository.StationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*

class SummaryViewModel @ViewModelInject constructor(
    private val repository: PumpRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val pumps: LiveData<List<PumpToSend>> = repository.pumps

    private val _dataState: MutableLiveData<Resource<Void>> = MutableLiveData()
    val dataState: LiveData<Resource<Void>>
        get() = _dataState

    fun submitPumps(
        pumps: RequestBody,
        branchId: RequestBody,
        managerName: RequestBody,
        phoneNumber: RequestBody,
        file: MultipartBody.Part
    ) = viewModelScope.launch {
        _dataState.value = repository.putPump(
            pumps,
            branchId,
            managerName,
            phoneNumber,
            file)

    }

    fun clearPumpDb() = viewModelScope.launch {
        repository.deletePump()
    }

}