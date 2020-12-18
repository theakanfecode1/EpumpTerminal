package com.epump.epumpterminal.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.epump.epumpterminal.models.PumpToSend


@Dao
interface PumpDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPump(pump: PumpToSend)

    @Query("SELECT * FROM pump_table")
    fun getPumps() : LiveData<List<PumpToSend>>


    @Query("DELETE FROM pump_table")
    suspend fun deleteAll()
}