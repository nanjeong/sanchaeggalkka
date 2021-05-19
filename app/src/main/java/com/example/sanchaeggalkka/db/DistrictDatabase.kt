package com.example.sanchaeggalkka.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AdministrativeDistrict::class], version = 1, exportSchema = false)
abstract class DistrictDatabase : RoomDatabase() {

    abstract val districtDatabaseDao: DistrictDao

    companion object {
        @Volatile
        private var INSTANCE: DistrictDatabase? = null

        fun getInstance(context: Context): DistrictDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DistrictDatabase::class.java,
                        "district_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}