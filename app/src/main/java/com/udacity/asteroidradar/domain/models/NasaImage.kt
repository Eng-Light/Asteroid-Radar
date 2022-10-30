package com.udacity.asteroidradar.domain.models

import com.udacity.asteroidradar.database.models.DataBaseNasaImage

data class NasaImage(
    val url: String,
    val media_type: String,
    val title: String
)

fun NasaImage.asDatabaseModel(): DataBaseNasaImage {
    return DataBaseNasaImage(
        url = url,
        media_type = media_type,
        title = title
    )
}