package com.epump.epumpterminal.models

import com.squareup.moshi.Json
import java.util.*


data class Station(
    @field : Json(name = "id") val id: String? = null,
    @field : Json(name = "name") val name: String?= null,
    @field : Json(name = "city") val city: String?= null,
    @field : Json(name = "state") val state: String?= null,
    @field : Json(name = "street") val street: String?= null
) {

}