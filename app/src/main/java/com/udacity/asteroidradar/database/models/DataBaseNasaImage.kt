package com.udacity.asteroidradar.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_nasa_image")
data class DataBaseNasaImage constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "media_type")
    val media_type: String,

    @ColumnInfo(name = "title")
    val title: String
)