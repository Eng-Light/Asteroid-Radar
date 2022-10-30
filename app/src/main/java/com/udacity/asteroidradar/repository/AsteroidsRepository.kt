package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.common.Constants
import com.udacity.asteroidradar.common.Result
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.NasaImage
import com.udacity.asteroidradar.domain.models.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) : BaseRepository() {
    private val dates = getNextSevenDaysFormattedDates()

    val sortedWeekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getSortedCurrentWeekAsteroids(dates[0])) {
            it?.asDomainModel()
        }
    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids(dates[0])) {
            it?.asDomainModel()
        }
    val allSortedAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) {
            it?.asDomainModel()
        }

    val loading = MutableLiveData(Constants.Visibility.GONE)
    val nasaImage: LiveData<NasaImage> =
        Transformations.map(database.asteroidDatabaseDao.getNasaImage()) {
            it?.asDomainModel()
        }

    suspend fun deleteAsteroidsBeforeToday() {
        withContext(Dispatchers.IO) {
            database.asteroidDatabaseDao.deleteAsteroidBefore(dates[0])
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidList = safeApiCall {
                AsteroidAPI.getInstance().getAsteroids(
                    dates[0], dates[dates.size - 1]
                )
            }
            when (asteroidList) {
                is Result.Success -> {
                    database.asteroidDatabaseDao.insertAll(*
                    withContext(Dispatchers.Default) {
                        parseAsteroidsJsonResult(
                            JSONObject(
                                asteroidList.data
                            )
                        ).asDatabaseModel()
                    })
                    loading.postValue(Constants.Visibility.GONE)
                }
                is Result.Loading -> {
                    loading.postValue(Constants.Visibility.VISIBLE)
                }
                is Result.Error -> {
                    loading.postValue(Constants.Visibility.GONE)
                    handleApiError(asteroidList)
                }
            }
        }
    }

    suspend fun refreshNasaImage() {
        withContext(Dispatchers.IO) {
            val imageResult = safeApiCall {
                AsteroidAPI.getInstance().getNasaImage()
            }
            when (imageResult) {
                is Result.Success -> {
                    database.asteroidDatabaseDao.insertImage(
                        withContext(Dispatchers.Default) {
                            parsNasaImageJsonResult(
                                JSONObject(
                                    imageResult.data
                                )
                            )
                        }.asDatabaseModel()
                    )
                    loading.postValue(Constants.Visibility.GONE)
                }
                is Result.Loading -> {
                    loading.postValue(Constants.Visibility.VISIBLE)
                }
                is Result.Error -> {
                    handleApiError(imageResult)
                    loading.postValue(Constants.Visibility.GONE)
                }
            }
        }
    }
}