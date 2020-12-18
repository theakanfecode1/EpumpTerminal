package com.epump.epumpterminal.api

import com.epump.epumpterminal.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EpumpService {
    @POST("/Account/Login")
    suspend fun loginUser(@Body user: User): Response<User>

    @GET("Branch/GetStations")
    suspend fun getStations(@Header("Authorization") auth: String): Response<List<Station>>

    @GET("/Branch/Pumps/{branchId}")
    suspend fun getPumps(
        @Path("branchId") branchId: String,
        @Header("Authorization") auth: String
    ): Response<List<Pump>>

    @POST("/Company/AddDevice")
    suspend fun addDevice(@Body device: Device,@Header("Authorization") auth: String): Response<Void>

    @GET("/Branch/Tanks/{branchId}")
    suspend fun getTanks(
        @Path("branchId") branchId: String,
        @Header("Authorization") auth: String
    ): Response<List<Tank>>

    @Multipart
    @POST("/QAConfiguration/Pump")
    suspend fun submitPumpDetails(
        @Part("pumps") qaConfig: RequestBody,
        @Part("branchId") branchId: RequestBody,
        @Part("managerName") managerName: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part signatureFile: MultipartBody.Part,
        @Header("Authorization") auth: String
    ) : Response<Void>


}