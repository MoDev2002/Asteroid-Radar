package com.udacity.asteroidradar.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getStartDate
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataManager(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids(getStartDate(), getEndDate())
            repository.getPictureOfTheDay()
            Result.success()
        }catch (e : HttpException) {
            Result.retry()
        }
    }
}