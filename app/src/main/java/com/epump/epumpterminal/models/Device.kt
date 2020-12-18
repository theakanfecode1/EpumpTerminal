package com.epump.epumpterminal.models

import com.squareup.moshi.Json

data class Device(
    @field: Json(name = "branchId") val branchId : String? = null,
    @field: Json(name = "deviceId") val deviceId : String? = null,
    @field: Json(name = "deviceType") val deviceType : String? = null,
    @field: Json(name = "pumpId") val pumpId : String? = null,
    @field: Json(name = "tankId") val tankId : String? = null,
    @field: Json(name = "probeAddress") val probeAddress : String? = null,
    @field: Json(name = "phoneNumber") val phoneNumber : String? = null,
    ) {
}

