package com.epump.epumpterminal.models

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Pump(
    var isChecked: Boolean? = null,
    @field: Json(name = "id") var id: String? = null,
    @field: Json(name = "tankName") var tankName: String? = null,
    @field: Json(name = "name") var name: String? = null,
    @field: Json(name = "displayName") var displayName: String? = null,
    @field: Json(name = "branchId") var branchId: String? = null,
    @field: Json(name = "digitalReading") var digitalReading: Double? = null,
    @field: Json(name = "manualReading") var manualReading: Double? = null,
    @field: Json(name = "price") var price: Double? = null,
    @field: Json(name = "productName") var productName: String? = null,
    @field: Json(name = "lastSeen") var lastSeen: String? = null,

    ) : Parcelable {

}

//{
//    "id": "6b7fe65a-180d-490d-9416-6689581c6f9c",
//    "name": "A1",
//    "displayName": "AGO 1",
//    "openingReading": null,
//    "currentReading": 139195.19999999998,
//    "currentManualReading": 0.0,
//    "branchId": "6a237865-91ec-4c58-8b75-d7333e4cb4e6",
//    "productName": "AGO",
//    "tankName": "AGO Tank 1",
//    "manufacturer": "Wayne",
//    "lastSeen": "48 days ago",
//    "lastUpdate": "2020-08-23T14:30:25.153",
//    "sellingPrice": 10.0,
//    "status": null,
//    "model": "Igem",
//    "ratio": 1.0,
//    "deviceName": null,
//    "deviceId": null,
//    "ipAddress": null,
//    "ssid": null,
//    "password": null,
//    "posMultiplierPrice": null,
//    "posMultiplierVolume": null
//},