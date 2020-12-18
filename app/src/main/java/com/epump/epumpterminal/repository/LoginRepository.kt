package com.epump.epumpterminal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.epump.epumpterminal.api.LoginDataSource
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val remoteDataSource: LoginDataSource,
    private val userDao: UserDao,

    ) {
    private  val TAG = "LoginRepository"
    var userDetails : LiveData<User> = userDao.getUser()

    suspend fun performUserLogin(
        user: User
    ): Flow<Resource<User>> =
        flow {
            emit(Resource.loading())
            val responseStatus = remoteDataSource.loginUserDataSource(user)
            if (responseStatus.status == Resource.Status.SUCCESS) {
                emit(Resource.success(responseStatus.data!!))
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.deleteAll()
                    userDao.insertUser(responseStatus.data)
//                    userDetails = userDao.getUser()
//                    Log.d(TAG, "performUserLogin: ${userDetails.value?.firstName},${userDetails.value?.token}")

                }


            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.message!!))
            }
        }

    suspend fun deleteAllUser(){
        userDao.deleteAll()
    }

}