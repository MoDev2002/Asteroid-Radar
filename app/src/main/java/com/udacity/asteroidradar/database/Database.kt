package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

/**
 * Database creation and initialization
 **/

@Database(entities = [DatabaseAsteroid::class], version = 1 , exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao : AsteroidDao
}

private lateinit var INSTANCE : AsteroidDatabase

@OptIn(InternalCoroutinesApi::class)
fun getDatabase(context: Context) : AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                                            AsteroidDatabase::class.java,
                                        "asteroid").build()
        }
    }
    return INSTANCE
}