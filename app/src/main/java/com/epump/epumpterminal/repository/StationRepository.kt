package com.epump.epumpterminal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.epump.epumpterminal.api.LoginDataSource
import com.epump.epumpterminal.api.StationDataSource
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.Device
import com.epump.epumpterminal.models.Station
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StationRepository @Inject constructor(
    private val remoteDataSource: StationDataSource,
    ) {


    suspend fun getStations(
    ): Flow<Resource<List<Station>>> =
        flow {
            emit(Resource.loading())
            val responseStatus = remoteDataSource.getStationsUserDataSource()
            if (responseStatus.status == Resource.Status.SUCCESS) {
                emit(Resource.success(responseStatus.data!!))

            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.message!!))
            }
        }

    suspend fun addDevice( device: Device
    ): Flow<Resource<Void?>> =
        flow {
            emit(Resource.loading())
            val responseStatus = remoteDataSource.addDeviceDataSource(device)
            if (responseStatus.status == Resource.Status.SUCCESS) {
                emit(Resource.success(responseStatus.data))

            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.message!!))
            }
        }

}