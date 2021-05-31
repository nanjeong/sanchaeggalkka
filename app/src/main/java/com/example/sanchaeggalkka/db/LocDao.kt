package com.example.sanchaeggalkka.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocDao {
    @Insert
    suspend fun insert(loc: Loc)

    @Update
    suspend fun update(loc: Loc)

    @Query("SELECT * FROM location_table")
    fun getAllLocation(): LiveData<List<Loc>>

    @Query("SELECT * FROM location_table WHERE id = :key")
    suspend fun get(key: Long): Loc

    @Query("SELECT * FROM location_table ORDER BY id DESC LIMIT 1")
    suspend fun getLast(): Loc

    @Query("SELECT lcName FROM location_table")
    fun getName(): LiveData<List<String>>

    @Delete
    suspend fun delete(loc: Loc)
}