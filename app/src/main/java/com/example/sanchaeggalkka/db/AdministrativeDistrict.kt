package com.example.sanchaeggalkka.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "district_table")
data class AdministrativeDistrict(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name1: String,
    val name2: String,
    val name3: String,
    val x: Int,
    val y: Int
)