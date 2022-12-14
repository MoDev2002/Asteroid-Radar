package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Repository class for linking the room database to the Api
 * so we can use the app when offline
 **/

class AsteroidRepository(private val database: AsteroidDatabase) {

    //Getting the list of asteroids from the database so we can use it the view model

    //Getting data from the Api and inserting it to the room database
    suspend fun refreshAsteroids(startDate : String, endDate : String) {
        withContext(Dispatchers.IO) {
            val apiAsteroids = Network.asteroidApi.getAsteroids(startDate, endDate).await()
            val apiAsteroidList = parseAsteroidsJsonResult(JSONObject(apiAsteroids.string()))
            database.asteroidDao.insertAllAsteroids(*apiAsteroidList.asDatabaseModel())
        }
    }

    suspend fun deleteOldAsteroids(todayDate : String){
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteOldAsteroids(todayDate)
        }
    }


    suspend fun getPictureOfTheDay() : PictureOfDay? {
        var imageOfDay : PictureOfDay
        withContext(Dispatchers.IO) {
           imageOfDay = Network.asteroidApi.photoOfTheDay().await()
        }
        if(imageOfDay.mediaType == Constants.PICTURE_OF_DAY){
            return imageOfDay
        }
        return null
    }

}