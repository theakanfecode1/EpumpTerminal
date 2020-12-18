package com.epump.epumpterminal.api

import com.epump.epumpterminal.models.User
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val epumpService: EpumpService
): BaseDataSource() {

    suspend fun loginUserDataSource(user : User) = getResult {
        epumpService.loginUser(user)
    }
}