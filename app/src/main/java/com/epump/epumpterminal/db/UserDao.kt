package com.epump.epumpterminal.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.epump.epumpterminal.models.User


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    fun getUser() : LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}