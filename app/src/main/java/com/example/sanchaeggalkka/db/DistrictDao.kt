package com.example.sanchaeggalkka.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DistrictDao {
    @Insert
    suspend fun insert(district: AdministrativeDistrict)

    @Query("SELECT * FROM district_table WHERE name1 = :name1 AND name2 = :name2 AND name3 = :name3")
    suspend fun get(name1: String, name2: String, name3: String): AdministrativeDistrict?

    @Query("SELECT * FROM district_table")
    suspend fun getAll(): List<AdministrativeDistrict>

    @Query("DELETE FROM district_table")
    suspend fun deleteAll()
}