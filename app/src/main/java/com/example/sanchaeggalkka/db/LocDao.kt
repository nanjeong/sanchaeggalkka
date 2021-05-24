package com.example.sanchaeggalkka.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocDao {
    @Insert
    suspend fun insert(loc: Loc)

    @Update
    suspend fun update(loc: Loc)

    @Query("SELECT * FROM location_table")
    fun getAllLocation(): LiveData<List<Loc>>

    @Query("SELECT * FROM location_table WHERE lcName = :key")
    suspend fun get(key: String): Loc

    @Query("SELECT lcName FROM location_table")
    fun getName(): LiveData<List<String>>
}