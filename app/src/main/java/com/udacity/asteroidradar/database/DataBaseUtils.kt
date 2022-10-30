package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.database.models.DataBaseNasaImage
import com.udacity.asteroidradar.database.models.DatabaseAsteroid
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.NasaImage


fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun DataBaseNasaImage.asDomainModel(): NasaImage {
    return NasaImage(
        url = url,
        media_type = media_type,
        title = title
    )
}