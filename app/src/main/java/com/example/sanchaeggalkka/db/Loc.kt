package com.example.sanchaeggalkka.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Loc(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val lcName: String,
    val name1: String,
    val name2: String,
    val name3: String,
    val x: Int,
    val y: Int,
    val current: Int
)