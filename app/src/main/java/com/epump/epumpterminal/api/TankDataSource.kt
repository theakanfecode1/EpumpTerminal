package com.epump.epumpterminal.api

import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.ui.SplashScreenFragment
import com.epump.epumpterminal.utils.Constants
import javax.inject.Inject

class TankDataSource @Inject constructor(
    private val epumpService: EpumpService
): BaseDataSource() {

    suspend fun getTanksDataSource(branchId : String) = getResult {
        epumpService.getTanks(branchId, Constants.BEARER+SplashScreenFragment.token)
    }
}