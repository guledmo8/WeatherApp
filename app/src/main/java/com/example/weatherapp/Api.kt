package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET( "weather")
    fun getCurrentConditions(
        @Query( "zip") zip: String,
        @Query( "units") units: String = "Imperial",
        @Query( "appId") appId: String = "5e7d9b53cd79cc03a42cb84a604eb799",
    ) : Call<CurrentConditions>

    @GET( "daily")
    fun getForecast(
        @Query( "zip") zip: String,
        @Query( "units") units: String = "Imperial",
        @Query( "appId") appId: String = "5e7d9b53cd79cc03a42cb84a604eb799",
        @Query( "cnt") cnt: String = "16"
    ) : Call<Forecast>
}