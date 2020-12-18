package com.epump.epumpterminal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "email") @field : Json(name = "email") val email : String? = null,
    @ColumnInfo(name = "username") @field : Json(name = "userName") val userName : String? = null,
    @ColumnInfo(name = "password") @field : Json(name = "password") val password : String? = null,
    @ColumnInfo(name = "role") @field : Json(name = "role") val role : String? = null,
    @ColumnInfo(name = "token") @field : Json(name = "token") val token : String? = null,
    @ColumnInfo(name = "firstName") @field : Json(name = "firstName") val firstName : String? = null,
    @ColumnInfo(name = "lastName") @field : Json(name = "lastName") val lastName : String? = null,
    @ColumnInfo(name = "phone") @field : Json(name = "phone") val phone : String? = null,
    ) {
}