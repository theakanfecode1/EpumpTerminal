package com.epump.epumpterminal.api

import android.util.Log
import com.epump.epumpterminal.ui.SplashScreenFragment
import com.epump.epumpterminal.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PumpsDataSource @Inject constructor(
    private val epumpService: EpumpService
): BaseDataSource() {

    suspend fun getPumpsUserDataSource(branchId : String) = getResult {
        epumpService.getPumps(branchId, Constants.BEARER+SplashScreenFragment.token)
    }

    suspend fun submitPumps(pumps: RequestBody,branchId: RequestBody,managerName : RequestBody,phoneNumber:RequestBody,file:MultipartBody.Part) = getResult {
        epumpService.submitPumpDetails(pumps,branchId,managerName,phoneNumber,file,Constants.BEARER+SplashScreenFragment.token)
    }
}