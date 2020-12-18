package com.epump.epumpterminal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.epump.epumpterminal.api.LoginDataSource
import com.epump.epumpterminal.api.PumpsDataSource
import com.epump.epumpterminal.api.StationDataSource
import com.epump.epumpterminal.api.TankDataSource
import com.epump.epumpterminal.db.PumpDao
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.*
import com.epump.epumpterminal.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TankRepository @Inject constructor(
    private val remoteDataSource: TankDataSource,
    ) {

    suspend fun getTanks( branchId : String
    ): Flow<Resource<List<Tank>>> =
        flow {
            emit(Resource.loading())
            val responseStatus = remoteDataSource.getTanksDataSource(branchId)
            if (responseStatus.status == Resource.Status.SUCCESS) {
                emit(Resource.success(responseStatus.data!!))

            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.message!!))
            }
        }


}