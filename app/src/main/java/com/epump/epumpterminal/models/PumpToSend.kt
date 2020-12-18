package com.epump.epumpterminal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "pump_table", indices = [Index(value = ["pumpId"], unique = true)])
data class PumpToSend(
    @PrimaryKey(autoGenerate = true) var dbId: Int,
    @ColumnInfo(name = "pumpId") @field: Json(name = "pumpId") var pumpId: String? = null,
    @ColumnInfo(name = "displayName") @field: Json(name = "displayName") var displayName: String? = null,
    @ColumnInfo(name = "digitalReading") @field: Json(name = "digitalReading") var digitalReading: Double? = null,
    @ColumnInfo(name = "manualReading") @field: Json(name = "manualReading") var manualReading: Double? = null,
    @ColumnInfo(name = "price") @field: Json(name = "price") var price: Double? = null,
    @ColumnInfo(name = "totalMultiplier") @field: Json(name = "totalMultiplier") var totalMultiplier: Double? = null,
    @ColumnInfo(name = "volumeMultiplier") @field: Json(name = "volumeMultiplier") var volumeMultiplier: Double? = null,
    @ColumnInfo(name = "amountMultiplier") @field: Json(name = "amountMultiplier") var amountMultiplier: Double? = null,

    ) {
}