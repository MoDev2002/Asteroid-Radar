package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getStartDate
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Assigning a job to the view model
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //Define the room database and the repository
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    //List of asteroids
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids : LiveData<List<Asteroid>>
        get() = _asteroids

    //Detail Screen navigation activity
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid : LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    fun displayDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayDetailsCompleted(){
        _navigateToSelectedAsteroid.value = null
    }
    //Getting picture of the day Info from api
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    //Adding Asteroids from the Api to the room database
    init {
        getSavedAsteroids()
        viewModelScope.launch {
            try {
                asteroidRepository.deleteOldAsteroids(getStartDate())
                asteroidRepository.refreshAsteroids(getStartDate(), getEndDate())
                _pictureOfDay.value = asteroidRepository.getPictureOfTheDay()
            }catch (e : HttpException) {
                throw IllegalArgumentException(e)
            }

        }
    }




    fun getWeekAsteroids() {
        viewModelScope.launch {
            database.asteroidDao.getAsteroidsByDate(getStartDate(), getEndDate()).collect { asteroids ->
                _asteroids.value = asteroids
            }
        }
    }

    fun getTodayAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getAsteroidsByDate(getStartDate(), getStartDate()).collect{ asteroids ->
                _asteroids.value = asteroids
            }
        }
    }

    fun getSavedAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getAsteroids().collect { asteroids ->
                _asteroids.value = asteroids
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}