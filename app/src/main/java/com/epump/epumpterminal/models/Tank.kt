package com.epump.epumpterminal.models

import com.squareup.moshi.Json

data class Tank(
    @field: Json(name = "id") var id: String? = null,
    @field: Json(name = "name") var name: String? = null,
    @field: Json(name = "productName") var productName: String? = null,
    ) {
}
