package com.example.weatherapp

import androidx.appcompat.app.ActionBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api): ViewModel() {
    private val theForecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = theForecast

    fun loadData(zipCode: String) = runBlocking {
        launch {
            theForecast.value = service.getForecast(zipCode)
        }

    }

    fun loadData(latitude: Float, longitude: Float) = runBlocking {
        launch{
            theForecast.value = service.getForecastLatLon(latitude, longitude)
        }
    }

}