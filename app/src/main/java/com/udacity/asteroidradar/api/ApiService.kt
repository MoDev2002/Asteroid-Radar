package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Getting the Nasa Asteroid Api using retrofit
 **/


interface AsteroidService {
    @GET("/neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date")
        startDate : String,
        @Query("end_date")
        endDate : String,
        @Query("api_key")
        apiKey : String = Constants.API_KEY
    ) : Deferred<ResponseBody>

    @GET("/planetary")
    fun photoOfTheDay(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : Deferred<PictureOfDay>
}

object Network {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidApi = retrofit.create(AsteroidService::class.java)
}