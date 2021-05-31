package com.example.sanchaeggalkka.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Loc(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var lcName: String,
    var name1: String,
    var name2: String,
    var name3: String,
    var x: Int,
    var y: Int,
    var current: Int = 1
)