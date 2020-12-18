package com.epump.epumpterminal.api

import com.epump.epumpterminal.models.Device
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.ui.SplashScreenFragment
import com.epump.epumpterminal.utils.Constants
import javax.inject.Inject

class StationDataSource @Inject constructor(
    private val epumpService: EpumpService
): BaseDataSource() {

    suspend fun getStationsUserDataSource() = getResult {
        epumpService.getStations(Constants.BEARER+SplashScreenFragment.token)
    }

    suspend fun addDeviceDataSource(device: Device) = getResult {
        epumpService.addDevice(device, Constants.BEARER+SplashScreenFragment.token)
    }
}