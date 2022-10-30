package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    val loading = asteroidsRepository.loading

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshNasaImage()
        }
    }

    val photo = asteroidsRepository.nasaImage
    val asteroidsList = asteroidsRepository.allSortedAsteroids
    val todayAsteroids = asteroidsRepository.todayAsteroids
    val weekAsteroids = asteroidsRepository.sortedWeekAsteroids

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw java.lang.IllegalArgumentException("Unable to construct view_model")
        }
    }
}