package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.api.getEndData
import com.udacity.asteroidradar.api.getStartDate
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Assigning a job to the view model
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //Define the room database and the repository
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    //Adding Asteroids from the Api to the room database
    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids(getStartDate(), getEndData())
        }
    }

    //Getting the list of asteroids from the repository
    val asteroids = asteroidRepository.asteroids
}