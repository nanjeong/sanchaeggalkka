package com.example.sanchaeggalkka.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AdministrativeDistrict::class, Loc::class], version = 1, exportSchema = true)
abstract class DistrictDatabase : RoomDatabase() {

    abstract val districtDao: DistrictDao
    abstract val locDao: LocDao

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
                    )
                        .createFromAsset("database/district.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}