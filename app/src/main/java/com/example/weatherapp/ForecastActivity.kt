package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_forecast.*


class ForecastActivity : AppCompatActivity() {

    val temperature = listOf<ForecastTemp>(
        ForecastTemp(16F, 11F, 20F),
        ForecastTemp(16F, 13F, 19F),
        ForecastTemp(23F, 18F, 27F),
        ForecastTemp(27F, 20F, 34F),
        ForecastTemp(22F, 17F, 27F),
        ForecastTemp(30F, 25F, 25F),
        ForecastTemp(18F, 12F, 24F),
        ForecastTemp(25F, 19F, 30F),
        ForecastTemp(11F, 7F, 14F),
        ForecastTemp(16F, 10F, 21F),
        ForecastTemp(14F, 9F, 18F),
        ForecastTemp(27F, 21F, 32F),
        ForecastTemp(5F, 0F, 10F),
        ForecastTemp(8F, 3F, 12F),
        ForecastTemp(28F, 22F, 34F),
        ForecastTemp(20F, 14F, 26F),
    )


    val adapterData = listOf<DayForecast>(
        DayForecast(1643589840, 1643549640, 1643584680, temperature.get(0), 30F, 71),
        DayForecast(1643676240, 1643549580, 1643584740, temperature.get(1), 29F, 69),
        DayForecast(1643762640, 1643549520, 1643584860, temperature.get(2), 30F, 66),
        DayForecast(1643849040, 1643549460, 1643584920, temperature.get(3), 29F, 58),
        DayForecast(1643895000, 1643895000, 1643930640, temperature.get(4), 28F, 53),
        DayForecast(1643981340, 1643981340, 1644017100, temperature.get(5), 28F, 68),
        DayForecast(1644067620, 1644067620, 1644103560, temperature.get(6), 27F, 81),
        DayForecast(1644153960, 1644153960, 1644190080, temperature.get(7), 29F, 69),
        DayForecast(1644240300, 1644240300, 1644276540, temperature.get(8), 30F, 74),
        DayForecast(1644326580, 1644326580, 1644363060, temperature.get(9), 30F, 78),
        DayForecast(1644412920, 1644412920, 1644449520, temperature.get(10), 30F, 87),
        DayForecast(1644499260, 1644499260, 1644536040, temperature.get(11), 29F, 59),
        DayForecast(1644585540, 1644585540, 1644622500, temperature.get(12), 29F, 66),
        DayForecast(1644671880, 1644671880, 1644708960, temperature.get(13), 28F, 74),
        DayForecast(1644758160, 1644758160, 1644795480, temperature.get(14), 29F, 72),
        DayForecast(1644844500, 1644844500, 1644881940, temperature.get(15), 30F, 80),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val actionBar = supportActionBar
        actionBar!!.title = "Forecast Activity"

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(adapterData)
    }
}