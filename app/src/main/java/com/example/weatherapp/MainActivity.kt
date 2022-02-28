package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val apiKey = "5e7d9b53cd79cc03a42cb84a604eb799"

    private lateinit var api: Api


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

        forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val call: Call<CurrentConditions> = api.getCurrentConditions("55423")
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentConditions = response.body()
                currentConditions?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {

            }

        })
    }

    private fun bindData(currentConditions: CurrentConditions) {
        city_name.text = currentConditions.name
        temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        feels_like.text = getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        low.text = getString(R.string.low, currentConditions.main.tempMin.toInt())
        high.text = getString(R.string.high, currentConditions.main.tempMax.toInt())
        humidity.text = getString(R.string.humidity, currentConditions.main.humidity.toInt())
        pressure.text = getString(R.string.pressure, currentConditions.main.pressure.toInt())

        val weather = currentConditions.weather.firstOrNull()
        weather?.let {
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                .into(condition_icon)
        }

    }
}