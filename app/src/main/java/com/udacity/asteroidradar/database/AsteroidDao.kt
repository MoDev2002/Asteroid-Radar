package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.repository.Asteroid
import kotlinx.coroutines.flow.Flow

/**
 * Room Database Dao that has basic database operations
 **/
@Dao
interface AsteroidDao {
    @Query("select * from DatabaseAsteroid order by closeApproachDate asc")
    fun getAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: DatabaseAsteroid)

    @Query("delete from DatabaseAsteroid where closeApproachDate < :todayDate")
    fun deleteOldAsteroids(todayDate: String)

    @Query("select * from DatabaseAsteroid where closeApproachDate >= :startDate and closeApproachDate <= :endDate order by closeApproachDate asc")
    fun getAsteroidsByDate(startDate: String, endDate: String): Flow<List<Asteroid>>
}