package com.epump.epumpterminal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.models.PumpToSend
import com.epump.epumpterminal.models.User

@Database(entities = arrayOf(User::class,PumpToSend::class), version = 1, exportSchema = false)
abstract class EpumpDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun pumpDao(): PumpDao


//    companion object {
//        @Volatile
//        private var INSTANCE: EpumpDatabase? = null
//        fun getDatabase(
//            context: Context,
//        ) : EpumpDatabase{
//            return INSTANCE ?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    EpumpDatabase::class.java,
//                    "epump_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//
//                INSTANCE = instance
//                instance
//
//            }
//        }
//
//    }
}