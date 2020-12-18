package com.epump.epumpterminal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.epump.epumpterminal.api.PumpsDataSource
import com.epump.epumpterminal.db.PumpDao
import com.epump.epumpterminal.models.*
import com.epump.epumpterminal.utils.Resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PumpRepository @Inject constructor(
    private val remoteDataSource: PumpsDataSource,
    private val pumpDao: PumpDao
) {
    val pumps: LiveData<List<PumpToSend>> = pumpDao.getPumps()

    suspend fun getPumps(
        branchId: String
    ): Flow<Resource<List<Pump>>> =
        flow {
            emit(Resource.loading())
            val responseStatus = remoteDataSource.getPumpsUserDataSource(branchId)
            if (responseStatus.status == Resource.Status.SUCCESS) {
                emit(Resource.success(responseStatus.data!!))

            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.message!!))
            }
        }

    suspend fun insertPump(pump: PumpToSend) {
        pumpDao.insertPump(pump)
    }

    suspend fun deletePump() {
        pumpDao.deleteAll()
    }


    suspend fun putPump(
        pumps: RequestBody,
        branchId: RequestBody,
        managerName: RequestBody,
        phoneNumber: RequestBody,
        file: MultipartBody.Part
    ): Resource<Void> =
          remoteDataSource.submitPumps(pumps, branchId, managerName, phoneNumber, file)


}