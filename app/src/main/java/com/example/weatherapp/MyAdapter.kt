package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateView: TextView = view.findViewById(R.id.date)
        private val sunriseView: TextView = view.findViewById(R.id.sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.sunset)
        private val dayTemp: TextView = view.findViewById(R.id.temp)
        private val tempMax: TextView = view.findViewById(R.id.high)
        private val tempMin: TextView = view.findViewById(R.id.low)
        private val forecastView: ImageView = view.findViewById(R.id.forecast_condition)

        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

        fun bind(data: DayForecast) {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.dt), ZoneId.systemDefault())
            val sunrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunrise), ZoneId.systemDefault())
            val sunset = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunset), ZoneId.systemDefault())
            val weather = data.weather.firstOrNull()?.icon
                Glide.with(forecastView)
                    .load("https://openweathermap.org/img/wn/${weather}@2x.png")
                    .into(forecastView)

            dateView.text = dateFormatter.format(dateTime)
            sunriseView.append(timeFormatter.format(sunrise))
            sunsetView.append(timeFormatter.format(sunset))
            dayTemp.append(data.temp.day.toInt().toString() + "°")
            tempMax.append(data.temp.max.toInt().toString() + "°")
            tempMin.append(data.temp.min.toInt().toString() + "°")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount() = data.size

}